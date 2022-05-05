package io.github.joke.objects;

import com.google.auto.service.AutoService;
import io.github.joke.objects.processor.AnnotationDispatcher;
import io.github.joke.objects.processor.Processor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.lang.model.SourceVersion.latestSupported;

@NotNull
@SupportedOptions({"debug"})
@SupportedAnnotationTypes("io.github.joke.objects.*")
@AutoService(javax.annotation.processing.Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private AnnotationDispatcher annotationDispatcher;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.annotationDispatcher = Processor.create(processingEnv).annotationDispatcher();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        annotationDispatcher.dispatch(roundEnv);
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

}
