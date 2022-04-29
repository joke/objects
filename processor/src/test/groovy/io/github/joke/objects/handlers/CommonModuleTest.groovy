package io.github.joke.objects.handlers

import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec.Builder
import io.github.joke.objects.generator.ClassGenerator
import io.github.joke.objects.generator.ConstructorGenerator
import io.github.joke.objects.generator.PropertiesGenerator
import io.github.joke.objects.generator.TypeGenerator
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment

import static io.github.joke.objects.handlers.CommonModule.provideConstructors
import static io.github.joke.objects.handlers.CommonModule.provideFields
import static io.github.joke.objects.handlers.CommonModule.provideFiler
import static io.github.joke.objects.handlers.CommonModule.provideGeneratedFields
import static io.github.joke.objects.handlers.CommonModule.provideJavaFile
import static io.github.joke.objects.handlers.CommonModule.provideMessager
import static io.github.joke.objects.handlers.CommonModule.provideTypeSpecBuilder

@Subject(CommonModule)
@Mockable([FieldSpec, MethodSpec, Builder])
class CommonModuleTest extends Specification {

    def 'provide generated fields'() {
        setup:
        PropertiesGenerator propertiesGenerator = DeepMock()
        FieldSpec fieldSpec = DeepMock()

        when:
        def res = provideGeneratedFields(propertiesGenerator)

        then:
        propertiesGenerator.properties >> [fieldSpec]

        expect:
        res == [fieldSpec]
    }

    def 'provide fields'() {
        setup:
        FieldSpec fieldSpec = DeepMock()

        def res = provideFields([fieldSpec])

        expect:
        res in HashSet
        res ==~ [fieldSpec]
    }

    def 'provide java file'() {
        setup:
        TypeGenerator typeGenerator = DeepMock()
        FieldSpec fieldSpec = DeepMock()

        when:
        def res = provideJavaFile(typeGenerator)

        then:
        typeGenerator.javaFile >> { [fieldSpec] as Set }

        expect:
        res in Set
        res ==~ [fieldSpec]
    }

    def 'provide filer'() {
        setup:
        ProcessingEnvironment processingEnvironment = DeepMock()
        Filer filer = DeepMock()

        when:
        def res = provideFiler(processingEnvironment)

        then:
        processingEnvironment.filer >> filer

        expect:
        res == filer
    }

    def 'provide messager'() {
        setup:
        ProcessingEnvironment processingEnvironment = DeepMock()
        Messager messager = DeepMock()

        when:
        def res = provideMessager(processingEnvironment)

        then:
        processingEnvironment.messager >> messager

        expect:
        res == messager
    }

    def 'provide type spec builder'() {
        setup:
        ClassGenerator classGenerator = DeepMock()
        Builder builder = DeepMock()

        when:
        def res = provideTypeSpecBuilder(classGenerator)

        then:
        classGenerator.typeBuilder >> builder

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
        constructorGenerator.constructors >> { [methodSpec] as Set }

        expect:
        res in Set
        res ==~ [methodSpec]
    }
}
