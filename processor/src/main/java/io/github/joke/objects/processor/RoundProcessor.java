package io.github.joke.objects.processor;

import io.github.joke.objects.handlers.Handler;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.processing.RoundEnvironment;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static javax.lang.model.util.ElementFilter.typesIn;

@Singleton
public class RoundProcessor {

    private final Processor processor;
    private final FileWriter fileWriter;
    private final Map<Class<? extends Annotation>, Handler.Factory<?>> handlerBuilders;

    @Inject
    public RoundProcessor(final Processor processor, final FileWriter fileWriter, final Map<Class<? extends Annotation>, Handler.Factory<?>> handlerFactories) {
        this.processor = processor;
        this.fileWriter = fileWriter;
        this.handlerBuilders = handlerFactories;
    }

    public void process(final RoundEnvironment roundEnvironment) {
        handlerBuilders.forEach((annotation, handler) -> processAndWrite(handler, getTypeElementsForAnnotation(roundEnvironment, annotation)));
    }

    @VisibleForTesting
    protected void processAndWrite(final Handler.Factory<?> handlerFactory, final Set<TypeElement> typeElements) {
        typeElements.stream()
                .map(typeElement -> handlerFactory.create(processor, typeElement))
                .map(Handler::process)
                .flatMap(Collection::stream)
                .forEach(fileWriter::write);
    }

    @VisibleForTesting
    protected Set<TypeElement> getTypeElementsForAnnotation(final RoundEnvironment roundEnvironment, final Class<? extends Annotation> annotation) {
        return typesIn(roundEnvironment.getElementsAnnotatedWith(annotation));
    }
}
