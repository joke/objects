package io.github.joke.objects.handlers;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.ClassGenerator;
import io.github.joke.objects.generator.ConstructorGenerator;
import io.github.joke.objects.generator.PropertiesGenerator;
import io.github.joke.objects.generator.TypeGenerator;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Named;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Module
@NotNull
public interface CommonModule {

    @Provides
    @Named("properties")
    static List<FieldSpec> provideGeneratedFields(final PropertiesGenerator propertiesGenerator) {
        return propertiesGenerator.getProperties();
    }

    @Provides
    @ElementsIntoSet
    static Set<FieldSpec> provideFields(@Named("properties") final List<FieldSpec> fieldSpecs) {
        return new HashSet<>(fieldSpecs);
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

    @Provides
    @ElementsIntoSet
    static Set<MethodSpec> provideConstructors(final ConstructorGenerator constructorGenerator) {
        return constructorGenerator.getConstructors();
    }
}
