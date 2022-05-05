package io.github.joke.objects.processor;

import dagger.BindsInstance;
import dagger.Component;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;

@NotNull
@Singleton
@Component(modules = ProcessorModule.class)
public interface Processor {

    AnnotationDispatcher annotationDispatcher();

    static Processor create(final ProcessingEnvironment processingEnvironment) {
        return DaggerProcessor.builder().processingEnvironment(processingEnvironment).build();
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder processingEnvironment(ProcessingEnvironment processingEnvironment);

        Processor build();
    }

}
