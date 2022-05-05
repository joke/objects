package io.github.joke.objects

import io.github.joke.objects.processor.AnnotationDispatcher
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

import static javax.lang.model.SourceVersion.RELEASE_8
import static javax.lang.model.SourceVersion.latestSupported
import static org.apache.commons.lang3.reflect.FieldUtils.readField
import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField

class AnnotationProcessorTest extends Specification {

    ProcessingEnvironment processingEnvironment = DeepMock()

    @Subject
    def processor = new AnnotationProcessor()

    def 'init prepares annotationDispatcher'() {
        when:
        processor.init(processingEnvironment)

        then:
        readField(processor, 'processingEnv', true) == processingEnvironment
        readField(processor, 'annotationDispatcher', true)
    }

    def 'supported annotations'() {
        setup:
        processor.init(processingEnvironment)

        when:
        def res = processor.supportedAnnotationTypes

        then:
        1 * processingEnvironment.sourceVersion >> RELEASE_8

        expect:
        res ==~ ['io.github.joke.objects.*']
    }

    def 'supported version'() {
        setup:
        processor.init(processingEnvironment)

        expect:
        processor.supportedSourceVersion == latestSupported()
    }

    def 'start dispatching'() {
        setup:
        RoundEnvironment roundEnvironment = DeepMock()
        AnnotationDispatcher annotationDispatcher = DeepMock()
        writeDeclaredField(processor, 'annotationDispatcher', annotationDispatcher, true)

        when:
        processor.process([] as Set, roundEnvironment)

        then:
        1 * annotationDispatcher.dispatch(roundEnvironment) >> {}
        0 * _
    }
}
