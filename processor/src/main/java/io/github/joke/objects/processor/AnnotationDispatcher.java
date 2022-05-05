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
public class AnnotationDispatcher {

    private final FileWriter fileWriter;
    private final Map<Class<? extends Annotation>, Handler.Builder<?, ?>> handlerBuilders;

    @Inject
    public AnnotationDispatcher(final FileWriter fileWriter, final Map<Class<? extends Annotation>, Handler.Builder<?, ?>> handlerBuilders) {
        this.fileWriter = fileWriter;
        this.handlerBuilders = handlerBuilders;
    }

    public void dispatch(final RoundEnvironment roundEnvironment) {
        handlerBuilders.forEach((annotation, handler) -> processAndWrite(handler, getTypeElementsForAnnotation(roundEnvironment, annotation)));
    }

    @VisibleForTesting
    protected void processAndWrite(final Handler.Builder<?, ?> handlerBuilder, final Set<TypeElement> typeElements) {
        typeElements.stream()
                .map(t -> handlerBuilder.typeElement(t))
                .map(builder -> builder.build())
                .map(h -> h.process())
                .flatMap(Collection::stream)
                .forEach(f -> fileWriter.write(f));
    }

    @VisibleForTesting
    protected Set<TypeElement> getTypeElementsForAnnotation(final RoundEnvironment roundEnvironment, final Class<? extends Annotation> annotation) {
        return typesIn(roundEnvironment.getElementsAnnotatedWith(annotation));
    }

}
