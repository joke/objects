package io.github.joke.objects.handlers;

import com.squareup.javapoet.MethodSpec;
import dagger.Component;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.GettersGenerator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import java.util.Set;

import static io.github.joke.objects.handlers.DaggerValueHandler_Factory.builder;

public class ValueHandler extends AbstractHandler {

    @Inject
    public ValueHandler(final ProcessingEnvironment processingEnvironment) {
        super(builder(), processingEnvironment);
    }

    @dagger.Module
    public interface Module {
        // all the generators

        @Provides
        @ElementsIntoSet
        static Set<MethodSpec> provideGetters(final GettersGenerator gettersGenerator) {
            return gettersGenerator.getGetters();
        }

    }

    @Component(modules = {Module.class, CommonModule.class})
    interface Factory extends AbstractHandler.Factory {
        @Component.Builder
        interface Builder extends AbstractHandler.Builder<Factory> {
        }
    }

}
