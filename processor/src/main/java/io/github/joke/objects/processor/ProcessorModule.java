package io.github.joke.objects.processor;

import com.squareup.javapoet.AnnotationSpec;
import dagger.Binds;
import dagger.MapKey;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.github.joke.objects.Immutable;
import io.github.joke.objects.Mutable;
import io.github.joke.objects.generator.GeneratedAnnotationGenerator;
import io.github.joke.objects.handlers.Handler;
import io.github.joke.objects.handlers.ImmutableHandler;
import io.github.joke.objects.handlers.MutableHandler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;

@dagger.Module(subcomponents = {ImmutableHandler.class, MutableHandler.class})
interface ProcessorModule {

    @Provides
    @Singleton
    static Filer provideFiler(final ProcessingEnvironment processingEnvironment) {
        return processingEnvironment.getFiler();
    }

    @Provides
    @Singleton
    static Messager provideMessager(final ProcessingEnvironment processingEnvironment) {
        return processingEnvironment.getMessager();
    }

    @Provides
    @Singleton
    @Named("generated")
    static AnnotationSpec provideGeneratedAnnotation(final GeneratedAnnotationGenerator generatedAnnotationGenerator) {
        return generatedAnnotationGenerator.getGeneratedAnnotation();
    }

    @Binds
    @IntoMap
    @Singleton
    @AnnotationKey(Immutable.class)
    Handler.Builder<?, ?> immutableHandler(ImmutableHandler.Builder immutableHandlerBuilder);

    @Binds
    @IntoMap
    @Singleton
    @AnnotationKey(Mutable.class)
    Handler.Builder<?, ?> mutableHandler(MutableHandler.Builder mutableHandlerBuilder);

    @MapKey
    @interface AnnotationKey {
        Class<? extends Annotation> value();
    }

}
