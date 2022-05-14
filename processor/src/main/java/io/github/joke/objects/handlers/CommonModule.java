package io.github.joke.objects.handlers;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.ConstructorGenerator;
import io.github.joke.objects.generator.ImplementationFileGenerator;
import io.github.joke.objects.generator.ImplementationTypeGenerator;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.generator.extractors.AttributeExtractor;
import io.github.joke.objects.processor.Processor;
import io.github.joke.objects.scopes.TypeElementScope;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Module
@NotNull
public interface CommonModule {

    @Provides
    @TypeElementScope
    static List<Attribute> provideAttributes(final AttributeExtractor attributeExtractor) {
        return attributeExtractor.getAttributes();
    }

    @Provides
    @TypeElementScope
    static List<AttributeHandler> provideAttributeComponents(
            final Processor processor,
            final Handler handler,
            final AttributeHandler.Factory<?> factory,
            final List<Attribute> attributes) {
        return attributes.stream()
                .map(attribute -> factory.create(processor, handler, attribute))
                .collect(toList());
    }

    @Provides
    @ElementsIntoSet
    @TypeElementScope
    static Set<FieldSpec> provideAttributeFields(final List<AttributeHandler> attributeHandlers) {
        return attributeHandlers.stream()
                .map(AttributeHandler::getFields)
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    @Provides
    @ElementsIntoSet
    @TypeElementScope
    static Set<MethodSpec> provideAttributeMethods(final List<AttributeHandler> attributeHandlers) {
        return attributeHandlers.stream()
                .map(AttributeHandler::getMethods)
                .flatMap(Collection::stream)
                .collect(toSet());
    }

    @Provides
    @TypeElementScope
    @ElementsIntoSet
    static Set<JavaFile> provideImplementation(final ImplementationFileGenerator implementationFileGenerator) {
        return implementationFileGenerator.getJavaFile();
    }

    @Provides
    @TypeElementScope
    static TypeSpec.Builder provideTypeSpecBuilder(final ImplementationTypeGenerator implementationTypeGenerator) {
        return implementationTypeGenerator.getTypeBuilder();
    }

    @Provides
    @TypeElementScope
    @ElementsIntoSet
    static Set<MethodSpec> provideConstructors(final ConstructorGenerator constructorGenerator) {
        return constructorGenerator.getConstructors();
    }
}
