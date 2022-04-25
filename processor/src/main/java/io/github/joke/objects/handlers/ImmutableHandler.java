package io.github.joke.objects.handlers;

import com.squareup.javapoet.MethodSpec;
import dagger.Component;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.GettersGenerator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import java.util.Set;

import static io.github.joke.objects.handlers.AbstractHandler.ProcessorTarget.IMMUTABLE;
import static io.github.joke.objects.handlers.DaggerImmutableHandler_Factory.builder;

@NotNull
public class ImmutableHandler extends AbstractHandler {

    @Inject
    public ImmutableHandler(final ProcessingEnvironment processingEnvironment) {
        super(builder(), processingEnvironment);
    }

    @dagger.Module
    public interface Module {
        // all the generators

        @Provides
        static ProcessorTarget provideProcessorTarget() {
            return IMMUTABLE;
        }

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
