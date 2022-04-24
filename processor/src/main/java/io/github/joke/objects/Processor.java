package io.github.joke.objects.processor;

import com.google.auto.service.AutoService;
import io.github.joke.objects.handlers.Handler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.AnnotationHandlerBuilderFactory.handlers;
import static javax.lang.model.SourceVersion.latestSupported;
import static javax.lang.model.util.ElementFilter.typesIn;

@NotNull
@SupportedOptions({"debug"})
@AutoService(javax.annotation.processing.Processor.class)
public class Processor extends AbstractProcessor {

    private Map<Class<? extends Annotation>, Handler> handlers;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.handlers = handlers(processingEnv);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        handlers.forEach((annotation, handler) -> typesIn(roundEnv.getElementsAnnotatedWith(annotation))
                .forEach(handler::process));
        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return handlers.keySet().stream()
                .map(Class::getCanonicalName)
                .collect(toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

}
