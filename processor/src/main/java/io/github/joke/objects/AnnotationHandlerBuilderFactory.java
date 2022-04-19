package io.github.joke.objects;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Component;
import dagger.MapKey;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import io.github.joke.objects.handlers.BeanHandler;
import io.github.joke.objects.handlers.Handler;
import io.github.joke.objects.handlers.ValueHandler;
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
        @AnnotationKey(Bean.class)
        Handler beanHandler(BeanHandler beanHandler);

        @Binds
        @IntoMap
        @ClassKey(Value.class)
        Handler valueHandler(ValueHandler valueHandler);

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
