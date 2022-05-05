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
import io.github.joke.objects.generator.ImplementationFileGenerator;
import io.github.joke.objects.generator.PropertiesGenerator;
import io.github.joke.objects.generator.scanner.Property;
import io.github.joke.objects.generator.scanner.PropertyScanner;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

@Module
@NotNull
public interface CommonModule {

    @Provides
    @ElementScope
    static List<Property> provideProperties(final PropertyScanner propertyScanner) {
        return propertyScanner.getProperties();
    }

    @Provides
    @ElementScope
    @ElementsIntoSet
    static Set<FieldSpec> provideFields(final PropertiesGenerator propertiesGenerator) {
        return propertiesGenerator.getProperties();
    }

    @Provides
    @ElementScope
    @ElementsIntoSet
    static Set<JavaFile> provideImplementation(final ImplementationFileGenerator implementationFileGenerator) {
        return implementationFileGenerator.getJavaFile();
    }

    @Provides
    @ElementScope
    static TypeSpec.Builder provideTypeSpecBuilder(final ClassGenerator classGenerator) {
        return classGenerator.getTypeBuilder();
    }

    @Provides
    @ElementScope
    @ElementsIntoSet
    static Set<MethodSpec> provideConstructors(final ConstructorGenerator constructorGenerator) {
        return constructorGenerator.getConstructors();
    }
}
