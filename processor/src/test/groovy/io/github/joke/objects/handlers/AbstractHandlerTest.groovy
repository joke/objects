package io.github.joke.objects.handlers

import io.github.joke.objects.handlers.AbstractHandler.Builder
import io.github.joke.objects.handlers.AbstractHandler.Factory
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

class AbstractHandlerTest extends Specification {

    Builder<? extends Factory> factoryBuilder = DeepMock()
    ProcessingEnvironment processingEnvironment = DeepMock()

    @Subject(AbstractHandler)
    AbstractHandler abstractHandler = new MyHandler(factoryBuilder, processingEnvironment, )

    def 'process'() {
        setup:
        TypeElement typeElement = DeepMock()
        Factory factory = DeepMock()
        FileWriter fileWriter = DeepMock()

        when:
        abstractHandler.process(typeElement)

        then:
        1 * factoryBuilder.processingEnvironment(processingEnvironment) >> factoryBuilder
        1 * factoryBuilder.typeElement(typeElement) >> factoryBuilder
        1 * factoryBuilder.build() >> factory
        1 * factory.create() >> fileWriter
        1 * fileWriter.write() >> {}
        0 * _
    }

    class MyHandler extends AbstractHandler {
        MyHandler(final Builder<? extends Factory> builder, final ProcessingEnvironment processingEnvironment) {
            super(builder, processingEnvironment)
        }
    }
}
