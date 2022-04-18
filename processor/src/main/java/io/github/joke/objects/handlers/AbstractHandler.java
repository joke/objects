package io.github.joke.objects.handlers;

import dagger.BindsInstance;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public abstract class AbstractHandler implements Handler {

    private final ProcessingEnvironment processingEnvironment;
    private final Builder factoryBuilder;

    public AbstractHandler(final Builder builder, final ProcessingEnvironment processingEnvironment) {
        this.factoryBuilder = builder;
        this.processingEnvironment = processingEnvironment;
    }

    public final void process(final TypeElement typeElement) {
        factoryBuilder
                .processingEnvironment(processingEnvironment)
                .typeElement(typeElement)
                .build()
                .write();
    }

    interface Factory {

        FileWriter write();

    }

    interface Builder<T extends Factory>  {

        @BindsInstance
        Builder processingEnvironment(ProcessingEnvironment processingEnvironment);

        @BindsInstance
        Builder typeElement(TypeElement typeElement);

        T build();
    }


}
