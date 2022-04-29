package io.github.joke.objects.handlers

import com.squareup.javapoet.MethodSpec
import io.github.joke.objects.generator.GettersGenerator
import io.github.joke.objects.handlers.ImmutableHandler.Factory.Builder
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment

import static io.github.joke.objects.handlers.AbstractHandler.ProcessorTarget.IMMUTABLE
import static io.github.joke.objects.handlers.ImmutableHandler.Module.provideGetters
import static io.github.joke.objects.handlers.ImmutableHandler.Module.provideProcessorTarget
import static org.apache.commons.lang3.reflect.FieldUtils.readField

@Mockable(MethodSpec)
class ImmutableHandlerTest extends Specification {

    ProcessingEnvironment processingEnvironment = DeepMock()

    @Subject(ImmutableHandler)
    def immutableHandler = new ImmutableHandler(processingEnvironment)

    def 'constructor'() {
        expect:
        readField(immutableHandler, 'processingEnvironment', true) == processingEnvironment
        readField(immutableHandler, 'factoryBuilder', true) in Builder
    }

    def 'module provide getters'() {
        expect:
        provideProcessorTarget() == IMMUTABLE
    }

    def 'module provide processor target'() {
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

}
