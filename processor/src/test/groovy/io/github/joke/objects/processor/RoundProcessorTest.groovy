package io.github.joke.objects.processor

import com.squareup.javapoet.JavaFile
import io.github.joke.objects.Immutable
import io.github.joke.objects.Mutable
import io.github.joke.objects.handlers.Handler.Factory
import io.github.joke.objects.handlers.ImmutableHandler
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

import static javax.lang.model.element.ElementKind.CLASS
import static javax.lang.model.element.ElementKind.FIELD
import static org.apache.commons.lang3.reflect.FieldUtils.readField

class RoundProcessorTest extends Specification {

    Processor processor = Mock()
    FileWriter fileWriter = Mock()
    Factory mutableHandlerFactory = Mock()
    Factory immutableHandlerFactory = Mock()
    def handlers = [(Mutable): mutableHandlerFactory, (Immutable): immutableHandlerFactory]

    @Subject
    def roundProcessor = Spy(new RoundProcessor(processor, fileWriter, handlers))

    RoundEnvironment roundEnvironment = DeepMock()

    def 'constructor'() {
        expect:
        readField(roundProcessor, 'fileWriter', true) == fileWriter
        readField(roundProcessor, 'handlerBuilders', true) == handlers
    }

    def 'start process'() {
        setup:
        TypeElement mutable = DeepMock()
        TypeElement immutable = DeepMock()
        RoundEnvironment roundEnvironment = DeepMock()

        when:
        roundProcessor.process(roundEnvironment)

        then:
        1 * roundProcessor.getTypeElementsForAnnotation(roundEnvironment, Mutable) >> { [mutable] as Set }
        1 * roundProcessor.getTypeElementsForAnnotation(roundEnvironment, Immutable) >> { [immutable] as Set }
        1 * roundProcessor.processAndWrite(mutableHandlerFactory, [mutable] as Set) >> {}
        1 * roundProcessor.processAndWrite(immutableHandlerFactory, [immutable] as Set) >> {}
        1 * roundProcessor._
        0 * _
    }

    def 'process and write'() {
        setup:
        TypeElement immutable1 = DeepMock()
        TypeElement immutable2 = DeepMock()

        ImmutableHandler immutableHandler = Mock()
        JavaFile javaFile1 = Mock()
        JavaFile javaFile2 = Mock()
        JavaFile javaFile3 = Mock()

        when:
        roundProcessor.processAndWrite(immutableHandlerFactory, [immutable1, immutable2] as Set)

        then:
        1 * roundProcessor._
        0 * _

        and:
        then:
        1 * immutableHandlerFactory.create(processor, immutable1) >> immutableHandler
        1 * immutableHandler.process() >> { [javaFile1] as Set }
        1 * fileWriter.write(javaFile1) >> {}
        0 * _

        and:
        then:
        1 * immutableHandlerFactory.create(processor, immutable2) >> immutableHandler
        1 * immutableHandler.process() >> { [javaFile2, javaFile3] as Set }
        1 * fileWriter.write(javaFile2) >> {}
        1 * fileWriter.write(javaFile3) >> {}
        0 * _
    }

    def 'get type elements for annotation'() {
        setup:
        TypeElement classAnnotated = Stub { kind >> CLASS }
        TypeElement wrongClass = Stub { kind >> FIELD }

        when:
        def res = roundProcessor.getTypeElementsForAnnotation(roundEnvironment, Immutable)

        then:
        1 * roundEnvironment.getElementsAnnotatedWith(Immutable) >> { [classAnnotated, wrongClass] as Set }
        1 * roundProcessor._
        0 * _

        expect:
        res ==~ [classAnnotated]
    }
}
