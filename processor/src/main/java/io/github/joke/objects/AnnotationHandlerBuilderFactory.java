package io.github.joke.objects;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.IntoMap;
import io.github.joke.objects.handlers.ImmutableHandler;
import io.github.joke.objects.handlers.MutableHandler;
import io.github.joke.objects.handlers.Handler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import java.lang.annotation.Annotation;
import java.util.Map;

@NotNull
public interface AnnotationHandlerBuilderFactory {

    static Map<Class<? extends Annotation>, Handler> handlers(final ProcessingEnvironment processingEnvironment) {
        return DaggerAnnotationHandlerBuilderFactory_Factory.builder()
                .processingEnvironment(processingEnvironment)
                .build()
                .handlers();
    }

    @Module
    interface HandlerModule {
        @Binds
        @IntoMap
        @AnnotationKey(Mutable.class)
        Handler mutableHandler(MutableHandler mutableHandler);

        @Binds
        @IntoMap
        @AnnotationKey(Immutable.class)
        Handler immutableHandler(ImmutableHandler immutableHandler);

    }

    @MapKey
    @interface AnnotationKey {
        Class<? extends Annotation> value();
    }

    @Component(modules = HandlerModule.class)
    interface Factory {
        Map<Class<? extends Annotation>, Handler> handlers();

        @Component.Builder
        interface Builder {

            @BindsInstance
            Builder processingEnvironment(ProcessingEnvironment processingEnvironment);

            Factory build();
        }

    }

}
