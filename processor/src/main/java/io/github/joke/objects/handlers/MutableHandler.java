package io.github.joke.objects.handlers;

import com.squareup.javapoet.MethodSpec;
import dagger.Component;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.GettersGenerator;
import io.github.joke.objects.generator.SettersGenerator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import java.util.Set;

import static io.github.joke.objects.handlers.AbstractHandler.ProcessorTarget.MUTABLE;

@NotNull
public class MutableHandler extends AbstractHandler {

    @Inject
    public MutableHandler(final ProcessingEnvironment processingEnvironment) {
        super(DaggerMutableHandler_Factory.builder(), processingEnvironment);
    }

    @dagger.Module
    public interface Module extends CommonModule {
        // all the generators

        @Provides
        static ProcessorTarget provideProcessorTarget() {
            return MUTABLE;
        }

        @Provides
        @ElementsIntoSet
        static Set<MethodSpec> provideGetters(final GettersGenerator gettersGenerator) {
            return gettersGenerator.getGetters();
        }

        @Provides
        @ElementsIntoSet
        static Set<MethodSpec> provideSetters(final SettersGenerator settersGenerator) {
            return settersGenerator.getSetters();
        }
    }

    @Component(modules = {Module.class, CommonModule.class})
    interface Factory extends AbstractHandler.Factory {
        @Component.Builder
        interface Builder extends AbstractHandler.Builder<Factory> {
        }
    }

}
