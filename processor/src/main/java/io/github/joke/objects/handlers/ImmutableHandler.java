package io.github.joke.objects.handlers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import dagger.Provides;
import dagger.Subcomponent;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.Target;
import io.github.joke.objects.generator.GettersGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static io.github.joke.objects.Target.IMMUTABLE;

@NotNull
@TypeElementScope
@Subcomponent(modules = {ImmutableHandler.Module.class, CommonModule.class})
public interface ImmutableHandler extends Handler {

    @Override
    Set<JavaFile> process();

    @dagger.Module
    interface Module {
        // additional components

        @Provides
        @TypeElementScope
        static Target provideProcessorTarget() {
            return IMMUTABLE;
        }

        @Provides
        @TypeElementScope
        @ElementsIntoSet
        static Set<MethodSpec> provideGetters(final GettersGenerator gettersGenerator) {
            return gettersGenerator.getGetters();
        }
    }

    @Subcomponent.Builder
    interface Builder extends Handler.Builder<ImmutableHandler, Builder> {
    }

}
