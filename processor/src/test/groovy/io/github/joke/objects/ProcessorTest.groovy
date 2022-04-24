package io.github.joke.objects

import io.github.joke.objects.handlers.BeanHandler
import io.github.joke.objects.handlers.Handler
import io.github.joke.objects.handlers.ValueHandler
import io.github.joke.objects.processor.Processor
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

import static javax.lang.model.SourceVersion.latestSupported
import static org.apache.commons.lang3.reflect.FieldUtils.readField
import static org.apache.commons.lang3.reflect.FieldUtils.writeDeclaredField

class ProcessorTest extends Specification {

    ProcessingEnvironment processingEnvironment = Mock()

    @Subject
    def processor = new Processor()

    def 'init prepares handler'() {
        setup:
        processor.init(processingEnvironment)

        expect:
        readField(processor, 'initialized', true)
        verifyAll(readField(processor, 'handlers', true) as Map) {
            size() == 2
            it[Bean] in BeanHandler
            it[Value] in ValueHandler
        }
    }

    def 'supported annotations'() {
        setup:
        processor.init(processingEnvironment)

        expect:
        annotation.canonicalName in processor.supportedAnnotationTypes

        where:
        annotation << [Value, Bean]
    }

    def 'supported version'() {
        setup:
        processor.init(processingEnvironment)

        expect:
        processor.supportedSourceVersion == latestSupported()
    }

    def 'start process'() {
        setup:
        Handler beanHandler = DeepMock()
        Handler valueHandler = DeepMock()
        def handlers = [(Bean): beanHandler, (Value): valueHandler]
        writeDeclaredField(processor, 'handlers', handlers, true)

        TypeElement bean = DeepMock()
        TypeElement value = DeepMock()
        RoundEnvironment roundEnvironment = DeepMock()

        TypeElement classAnnotated = Stub()
        TypeElement valueAnnotated1 = Stub()
        TypeElement valueAnnotated2 = Stub()

        when:
        def res = processor.process([bean, value] as Set, roundEnvironment)

        then:
        1 * roundEnvironment.getElementsAnnotatedWith(Bean) >> { [classAnnotated] as Set }
        1 * roundEnvironment.getElementsAnnotatedWith(Value) >> { [valueAnnotated1, valueAnnotated2] as Set }
        1 * beanHandler.process(classAnnotated) >> {}
        1 * valueHandler.process(valueAnnotated1) >> {}
        1 * valueHandler.process(valueAnnotated2) >> {}
        0 * _

        expect:
        res
    }
}
