package io.github.joke.objects.handlers;

import com.squareup.javapoet.MethodSpec;
import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.Target;
import io.github.joke.objects.generator.GetterGenerator;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.processor.Processor;
import io.github.joke.objects.scopes.AttributeScope;
import io.github.joke.objects.scopes.TypeElementScope;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static io.github.joke.objects.Target.IMMUTABLE;

@NotNull
@TypeElementScope
@Component(dependencies = Processor.class, modules = {ImmutableHandler.Module.class, CommonModule.class})
public interface ImmutableHandler extends Handler {

    @dagger.Module
    interface Module {
        // additional components

        @Provides
        @TypeElementScope
        static Target provideProcessorTarget() {
            return IMMUTABLE;
        }

        @Binds
        @TypeElementScope
        @SuppressWarnings("unused")
        Handler provideHandler(ImmutableHandler immutableHandler);

        @Provides
        @TypeElementScope
        static AttributeHandler.Factory<?> provideAttributeComponentFactory() {
            return DaggerImmutableHandler_ImmutableAttributeHandler.factory();
        }
    }

    @Component.Factory
    interface Factory extends Handler.Factory<ImmutableHandler> {
    }

    @AttributeScope
    @Component(dependencies = {Processor.class, Handler.class}, modules = {ImmutableAttributeHandler.AttributeModule.class})
    interface ImmutableAttributeHandler extends AttributeHandler {

        @Component.Factory
        interface Factory extends AttributeHandler.Factory<ImmutableAttributeHandler> {
            @Override
            ImmutableAttributeHandler create(Processor processor, Handler handler, @BindsInstance Attribute attribute);
        }

        @dagger.Module(includes = AttributeCommonModule.class)
        interface AttributeModule {

            @Provides
            @AttributeScope
            @ElementsIntoSet
            static Set<MethodSpec> provideGetter(final GetterGenerator generator) {
                return generator.generate();
            }
        }
    }
}
