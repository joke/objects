package io.github.joke.objects.processor;

import com.squareup.javapoet.AnnotationSpec;
import dagger.MapKey;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import io.github.joke.objects.Immutable;
import io.github.joke.objects.Mutable;
import io.github.joke.objects.generator.GeneratedAnnotationGenerator;
import io.github.joke.objects.handlers.DaggerImmutableHandler;
import io.github.joke.objects.handlers.DaggerMutableHandler;
import io.github.joke.objects.handlers.Handler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;

@dagger.Module
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
    static Types provideTypes(final ProcessingEnvironment processingEnvironment) {
        return processingEnvironment.getTypeUtils();
    }

    @Provides
    @Singleton
    static Elements provideElements(final ProcessingEnvironment processingEnvironment) {
        return processingEnvironment.getElementUtils();
    }

    @Provides
    @Singleton
    @Named("generated")
    static AnnotationSpec provideGeneratedAnnotation(final GeneratedAnnotationGenerator generatedAnnotationGenerator) {
        return generatedAnnotationGenerator.generate();
    }

    @IntoMap
    @Provides
    @Singleton
    @AnnotationKey(Immutable.class)
    static Handler.Factory<?> immutableHandler() {
        return DaggerImmutableHandler.factory();
    }

    @IntoMap
    @Provides
    @AnnotationKey(Mutable.class)
    static Handler.Factory<?> mutableHandler() {
        return DaggerMutableHandler.factory();
    }

    @MapKey
    @interface AnnotationKey {
        Class<? extends Annotation> value();
    }

}
