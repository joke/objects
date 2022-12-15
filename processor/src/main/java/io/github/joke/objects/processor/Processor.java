package io.github.joke.objects.processor;

import com.squareup.javapoet.AnnotationSpec;
import dagger.BindsInstance;
import dagger.Component;
import io.github.joke.objects.generator.extractors.AnnotationExtractor;
import io.github.joke.objects.utils.AnnotationUtils;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@NotNull
@Singleton
@Component(modules = ProcessorModule.class)
public abstract class Processor {

    @Named("generated")
    public abstract AnnotationSpec generatedAnnotation();
    public abstract Types types();
    public abstract Elements elements();
    public abstract AnnotationExtractor annotationExtractor();
    public abstract AnnotationUtils annotationUtils();
    protected abstract RoundProcessor roundProcessor();

    public static RoundProcessor create(final ProcessingEnvironment processingEnvironment) {
        return DaggerProcessor.factory()
                .create(processingEnvironment)
                .roundProcessor();
    }

    @Component.Factory
    interface Factory {
        Processor create(@BindsInstance ProcessingEnvironment processingEnvironment);
    }
}
