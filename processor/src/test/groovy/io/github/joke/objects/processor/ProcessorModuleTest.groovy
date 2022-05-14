package io.github.joke.objects.processor

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.FieldSpec
import io.github.joke.objects.generator.GeneratedAnnotationGenerator
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

import static io.github.joke.objects.processor.ProcessorModule.provideElements
import static io.github.joke.objects.processor.ProcessorModule.provideFiler
import static io.github.joke.objects.processor.ProcessorModule.provideGeneratedAnnotation
import static io.github.joke.objects.processor.ProcessorModule.provideMessager
import static io.github.joke.objects.processor.ProcessorModule.provideTypes

@Subject(ProcessorModule)
@Mockable([FieldSpec, AnnotationSpec])
class ProcessorModuleTest extends Specification {

    def 'provide filer'() {
        setup:
        Filer filer = DeepMock()
        ProcessingEnvironment processingEnvironment = DeepMock()

        when:
        def res = provideFiler(processingEnvironment)

        then:
        1 * processingEnvironment.filer >> filer
        0 * _

        expect:
        res == filer
    }

    def 'provide messager'() {
        setup:
        Messager messager = DeepMock()
        ProcessingEnvironment processingEnvironment = DeepMock()

        when:
        def res = provideMessager(processingEnvironment)

        then:
        1 * processingEnvironment.messager >> messager
        0 * _

        expect:
        res == messager
    }

    def 'provide types'() {
        setup:
        Types types = DeepMock()
        ProcessingEnvironment processingEnvironment = DeepMock()

        when:
        def res = provideTypes(processingEnvironment)

        then:
        1 * processingEnvironment.typeUtils >> types
        0 * _

        expect:
        res == types
    }

    def 'provide elements'() {
        setup:
        Elements elements = DeepMock()
        ProcessingEnvironment processingEnvironment = DeepMock()

        when:
        def res = provideElements(processingEnvironment)

        then:
        1 * processingEnvironment.elementUtils >> elements
        0 * _

        expect:
        res == elements
    }

    def 'provide generated annotation'() {
        setup:
        AnnotationSpec annotationSpec = DeepMock()
        GeneratedAnnotationGenerator generatedAnnotationGenerator = DeepMock()

        when:
        def res = provideGeneratedAnnotation(generatedAnnotationGenerator)

        then:
        1 * generatedAnnotationGenerator.generatedAnnotation >> annotationSpec
        0 * _

        expect:
        res == annotationSpec
    }

}
