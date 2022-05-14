package io.github.joke.objects.handlers

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec.Builder
import io.github.joke.objects.generator.ConstructorGenerator
import io.github.joke.objects.generator.ImplementationFileGenerator
import io.github.joke.objects.generator.ImplementationTypeGenerator
import io.github.joke.objects.generator.extractors.Attribute
import io.github.joke.objects.generator.extractors.AttributeExtractor
import spock.lang.Specification
import spock.lang.Subject

@Subject(CommonModule)
class CommonModuleTest extends Specification {

    Attribute attribute = Mock()
    FieldSpec fieldSpec = Mock()

    def 'provide provide properties'() {
        setup:
        AttributeHandler attributeComponent = Mock()
        AttributeExtractor attributeExtractor = Mock()

        when:
        def res = CommonModule.provideAttributes(attributeExtractor)

        then:
        1 * attributeExtractor.attributes >> [attribute]
        0 * _

        expect:
        res ==~ [attribute]
    }

    def 'provide fields'() {
        setup:
        AttributeHandler attributeHandler = DeepMock()

        when:
        def res = CommonModule.provideAttributeFields([attributeHandler])

        then:
        1 * attributeHandler.fields >> [fieldSpec]
        0 * _

        expect:
        res ==~ [fieldSpec]
    }

    def 'provide java file'() {
        setup:
        ImplementationFileGenerator typeGenerator = DeepMock()
        FieldSpec fieldSpec = DeepMock()

        when:
        def res = CommonModule.provideImplementation(typeGenerator)

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
        def res = CommonModule.provideTypeSpecBuilder(classGenerator)

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
        def res = CommonModule.provideConstructors(constructorGenerator)

        then:
        1 * constructorGenerator.constructors >> { [methodSpec] as Set }
        0 * _

        expect:
        res in Set
        res ==~ [methodSpec]
    }
}
