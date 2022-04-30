package io.github.joke.objects.handlers;

import dagger.BindsInstance;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

@NotNull
abstract class AbstractHandler implements Handler {

    private final ProcessingEnvironment processingEnvironment;
    private final Builder<? extends Factory> factoryBuilder;

    AbstractHandler(final Builder<? extends Factory> builder, final ProcessingEnvironment processingEnvironment) {
        this.factoryBuilder = builder;
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public final void process(final TypeElement typeElement) {
        factoryBuilder
                .processingEnvironment(processingEnvironment)
                .typeElement(typeElement)
                .build()
                .create()
                .write();
    }

    interface Factory {

        FileWriter create();

    }

    interface Builder<T extends Factory>  {

        @BindsInstance
        Builder<?> processingEnvironment(ProcessingEnvironment processingEnvironment);

        @BindsInstance
        Builder<?> typeElement(TypeElement typeElement);

        T build();
    }

    public enum ProcessorTarget {
        MUTABLE,
        IMMUTABLE
    }

}
