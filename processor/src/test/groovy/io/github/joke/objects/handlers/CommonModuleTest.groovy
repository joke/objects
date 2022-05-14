package io.github.joke.objects.handlers

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec.Builder
import io.github.joke.objects.generator.ImplementationTypeGenerator
import io.github.joke.objects.generator.ConstructorGenerator
import io.github.joke.objects.generator.ImplementationFileGenerator
import io.github.joke.objects.generator.PropertiesGenerator
import io.github.joke.objects.generator.scanner.Property
import io.github.joke.objects.generator.scanner.PropertyScanner
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import static io.github.joke.objects.handlers.CommonModule.provideConstructors
import static io.github.joke.objects.handlers.CommonModule.provideFields
import static io.github.joke.objects.handlers.CommonModule.provideImplementation
import static io.github.joke.objects.handlers.CommonModule.provideProperties
import static io.github.joke.objects.handlers.CommonModule.provideTypeSpecBuilder

@Subject(CommonModule)
@Mockable([FieldSpec, MethodSpec, Builder])
class CommonModuleTest extends Specification {

    def 'provide provide properties'() {
        setup:
        Property property = DeepMock()
        PropertyScanner propertyScanner = DeepMock()

        when:
        def res = provideProperties(propertyScanner)

        then:
        1 * propertyScanner.properties >> [property]
        0 * _

        expect:
        res == [property]
    }

    def 'provide fields'() {
        setup:
        Property property = DeepMock()
        PropertiesGenerator propertiesGenerator = DeepMock()

        when:
        def res = provideFields(propertiesGenerator)

        then:
        1 * propertiesGenerator.properties >> { [property] as Set }
        0 * _

        expect:
        res in HashSet
        res ==~ [property]
    }

    def 'provide java file'() {
        setup:
        ImplementationFileGenerator typeGenerator = DeepMock()
        FieldSpec fieldSpec = DeepMock()

        when:
        def res = provideImplementation(typeGenerator)

        then:
        1 * typeGenerator.javaFile >> { [fieldSpec] as Set }
        0 * _

        expect:
        res in Set
        res ==~ [fieldSpec]
    }

    def 'provide type spec builder'() {
        setup:
        ImplementationTypeGenerator classGenerator = DeepMock()
        Builder builder = DeepMock()

        when:
        def res = provideTypeSpecBuilder(classGenerator)

        then:
        1 * classGenerator.typeBuilder >> builder
        0 * _

        expect:
        res == builder
    }

    def 'provide constructors'() {
        setup:
        ConstructorGenerator constructorGenerator = DeepMock()
        MethodSpec methodSpec = DeepMock()

        when:
        def res = provideConstructors(constructorGenerator)

        then:
        1 * constructorGenerator.constructors >> { [methodSpec] as Set }
        0 * _

        expect:
        res in Set
        res ==~ [methodSpec]
    }
}
