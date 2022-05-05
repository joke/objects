package io.github.joke.objects.processor

import com.squareup.javapoet.JavaFile
import io.github.joke.objects.Immutable
import io.github.joke.objects.Mutable
import io.github.joke.objects.handlers.Handler
import io.github.joke.objects.handlers.ImmutableHandler
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

import static javax.lang.model.element.ElementKind.CLASS
import static javax.lang.model.element.ElementKind.FIELD
import static org.apache.commons.lang3.reflect.FieldUtils.readField

class AnnotationDispatcherTest extends Specification {

    FileWriter fileWriter = DeepMock()
    Handler.Builder mutableHandlerBuilder = Mock()
    Handler.Builder immutableHandlerBuilder = Mock()
    def handlers = [(Mutable): mutableHandlerBuilder, (Immutable): immutableHandlerBuilder]

    @Subject
    def annotationDispatcher = Spy(new AnnotationDispatcher(fileWriter, handlers))

    RoundEnvironment roundEnvironment = DeepMock()

    def 'constructor'() {
        expect:
        readField(annotationDispatcher, 'fileWriter', true) == fileWriter
        readField(annotationDispatcher, 'handlerBuilders', true) == handlers
    }

    def 'start process'() {
        setup:
        TypeElement mutable = DeepMock()
        TypeElement immutable = DeepMock()
        RoundEnvironment roundEnvironment = DeepMock()

        when:
        annotationDispatcher.dispatch(roundEnvironment)

        then:
        1 * annotationDispatcher.getTypeElementsForAnnotation(roundEnvironment, Mutable) >> { [mutable] as Set }
        1 * annotationDispatcher.getTypeElementsForAnnotation(roundEnvironment, Immutable) >> { [immutable] as Set }
        1 * annotationDispatcher.processAndWrite(mutableHandlerBuilder, [mutable] as Set) >> {}
        1 * annotationDispatcher.processAndWrite(immutableHandlerBuilder, [immutable] as Set) >> {}
        1 * annotationDispatcher._
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
        annotationDispatcher.processAndWrite(immutableHandlerBuilder, [immutable1, immutable2] as Set)

        then:
        1 * annotationDispatcher._
        0 * _

        and:
        then:
        1 * immutableHandlerBuilder.typeElement(immutable1) >> immutableHandlerBuilder
        1 * immutableHandlerBuilder.build() >> immutableHandler
        1 * immutableHandler.process() >> { [javaFile1] as Set }
        1 * fileWriter.write(javaFile1) >> {}
        0 * _

        and:
        then:

        1 * immutableHandlerBuilder.typeElement(immutable2) >> immutableHandlerBuilder
        1 * immutableHandlerBuilder.build() >> immutableHandler
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
        def res = annotationDispatcher.getTypeElementsForAnnotation(roundEnvironment, Immutable)

        then:
        1 * roundEnvironment.getElementsAnnotatedWith(Immutable) >> { [classAnnotated, wrongClass] as Set }
        1 * annotationDispatcher._
        0 * _

        expect:
        res ==~ [classAnnotated]
    }
}
