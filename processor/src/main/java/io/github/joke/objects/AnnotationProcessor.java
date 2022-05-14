package io.github.joke.objects;

import com.google.auto.service.AutoService;
import io.github.joke.objects.processor.Processor;
import io.github.joke.objects.processor.RoundProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static javax.lang.model.SourceVersion.latestSupported;

@NotNull
//@SupportedOptions({"io.github.joke.objects.debug"})
@SupportedAnnotationTypes("io.github.joke.objects.*")
@AutoService(javax.annotation.processing.Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    @Nullable
    private RoundProcessor roundProcessor;

    @Override
    public synchronized void init(final ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        roundProcessor = Processor.create(processingEnv);
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
        requireNonNull(roundProcessor, "Annotation processor initialization incomplete")
                .process(roundEnv);
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

}
