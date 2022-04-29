package io.github.joke.objects.handlers

import com.squareup.javapoet.MethodSpec
import io.github.joke.objects.generator.GettersGenerator
import io.github.joke.objects.generator.SettersGenerator
import io.github.joke.objects.handlers.MutableHandler.Factory.Builder
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment

import static io.github.joke.objects.handlers.AbstractHandler.ProcessorTarget.MUTABLE
import static io.github.joke.objects.handlers.MutableHandler.Module.provideGetters
import static io.github.joke.objects.handlers.MutableHandler.Module.provideProcessorTarget
import static io.github.joke.objects.handlers.MutableHandler.Module.provideSetters
import static org.apache.commons.lang3.reflect.FieldUtils.readField

@Mockable(MethodSpec)
class MutableHandlerTest extends Specification {

    ProcessingEnvironment processingEnvironment = DeepMock()

    @Subject(MutableHandler)
    def mutableHandler = new MutableHandler(processingEnvironment)

    def 'constructor'() {
        expect:
        readField(mutableHandler, 'processingEnvironment', true) == processingEnvironment
        readField(mutableHandler, 'factoryBuilder', true) in Builder
    }

    def 'module provide processor target'() {
        expect:
        provideProcessorTarget() == MUTABLE
    }

    def 'module provide getters'() {
        setup:
        GettersGenerator gettersGenerator = DeepMock()
        MethodSpec methodSpec = DeepMock()

        when:
        def res = provideGetters(gettersGenerator)

        then:
        1 * gettersGenerator.getters >> { [methodSpec] as Set }

        expect:
        res ==~ [methodSpec]
    }

    def 'module provide provide setters'() {
        setup:
        SettersGenerator settersGenerator = DeepMock()
        MethodSpec methodSpec = DeepMock()

        when:
        def res = provideSetters(settersGenerator)

        then:
        1 * settersGenerator.setters >> { [methodSpec] as Set }

        expect:
        res ==~ [methodSpec]
    }

}
