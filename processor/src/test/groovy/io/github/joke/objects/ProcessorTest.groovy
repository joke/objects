package io.github.joke.objects

import io.github.joke.objects.handlers.MutableHandler
import io.github.joke.objects.handlers.Handler
import io.github.joke.objects.handlers.ImmutableHandler
import io.github.joke.objects.processor.Processor
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

import static javax.lang.model.SourceVersion.latestSupported
import static javax.lang.model.element.ElementKind.CLASS
import static javax.lang.model.element.ElementKind.FIELD
import static org.apache.commons.lang3.reflect.FieldUtils.readField
import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField

class ProcessorTest extends Specification {

    ProcessingEnvironment processingEnvironment = Mock()

    @Subject
    def processor = new Processor()

    def 'init prepares handler'() {
        setup:
        processor.init(processingEnvironment)
        def handlers = readField(processor, 'handlers', true) as Map

        expect:
        readField(processor, 'processingEnv', true) == processingEnvironment
        verifyAll(handlers) {
            size() == 2
            it[Mutable] in MutableHandler
            it[Immutable] in ImmutableHandler
        }
    }

    def 'supported annotations'() {
        setup:
        processor.init(processingEnvironment)

        expect:
        annotation.canonicalName in processor.supportedAnnotationTypes

        where:
        annotation << [Immutable, Mutable]
    }

    def 'supported version'() {
        setup:
        processor.init(processingEnvironment)

        expect:
        processor.supportedSourceVersion == latestSupported()
    }

    def 'start process'() {
        setup:
        Handler mutableHandler = DeepMock()
        Handler immutableHandler = DeepMock()
        def handlers = [(Mutable): mutableHandler, (Immutable): immutableHandler]
        writeDeclaredField(processor, 'handlers', handlers, true)

        TypeElement mutable = DeepMock()
        TypeElement immutable = DeepMock()
        RoundEnvironment roundEnvironment = DeepMock()

        TypeElement classAnnotated = Stub { kind >> CLASS }
        TypeElement valueAnnotated1 = Stub { kind >> CLASS }
        TypeElement valueAnnotated2 = Stub { kind >> CLASS }
        TypeElement wrongClass = Stub { kind >> FIELD }

        when:
        def res = processor.process([mutable, immutable] as Set, roundEnvironment)

        then:
        1 * roundEnvironment.getElementsAnnotatedWith(Mutable) >> { [classAnnotated, wrongClass] as Set }
        1 * roundEnvironment.getElementsAnnotatedWith(Immutable) >> { [valueAnnotated1, valueAnnotated2] as Set }
        1 * mutableHandler.process(classAnnotated) >> {}
        1 * immutableHandler.process(valueAnnotated1) >> {}
        1 * immutableHandler.process(valueAnnotated2) >> {}
        0 * _

        expect:
        res
    }
}
