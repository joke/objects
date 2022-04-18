package io.github.joke.objects.handlers;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.ClassGenerator;
import io.github.joke.objects.generator.PropertiesGenerator;
import io.github.joke.objects.generator.TypeGenerator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import java.util.Set;

@Module
public interface CommonModule {
    @Provides
    @ElementsIntoSet
    static Set<FieldSpec> provideProperties(final PropertiesGenerator propertiesGenerator) {
        return propertiesGenerator.getProperties();
    }

    @Provides
    static JavaFile provideJavaFile(final TypeGenerator typeGenerator) {
        return typeGenerator.getJavaFile();
    }

    @Provides
    static Filer provideFiler(final ProcessingEnvironment processingEnvironment) {
        return processingEnvironment.getFiler();
    }

    @Provides
    static Messager provideMessager(final ProcessingEnvironment processingEnvironment) {
        return processingEnvironment.getMessager();
    }

    @Provides
    static TypeSpec.Builder provideTypeSpecBuilder(final ClassGenerator classGenerator) {
        return classGenerator.getTypeBuilder();
    }
}
