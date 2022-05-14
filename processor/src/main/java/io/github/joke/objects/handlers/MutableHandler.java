package io.github.joke.objects.handlers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.Target;
import io.github.joke.objects.generator.GettersGenerator;
import io.github.joke.objects.generator.SettersGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static io.github.joke.objects.Target.MUTABLE;

@NotNull
@TypeElementScope
@Subcomponent(modules = {MutableHandler.Module.class, CommonModule.class})
public interface MutableHandler extends Handler {

    @Override
    Set<JavaFile> process();

    @dagger.Module
    interface Module {
        // additional components

        @Provides
        @TypeElementScope
        static Target provideProcessorTarget() {
            return MUTABLE;
        }

        @Provides
        @TypeElementScope
        @ElementsIntoSet
        static Set<MethodSpec> provideGetters(final GettersGenerator gettersGenerator) {
            return gettersGenerator.getGetters();
        }

        @Provides
        @TypeElementScope
        @ElementsIntoSet
        static Set<MethodSpec> provideSetters(final SettersGenerator settersGenerator) {
            return settersGenerator.getSetters();
        }
    }

    @Subcomponent.Builder
    interface Builder extends Handler.Builder<MutableHandler, Builder> {
    }

}
