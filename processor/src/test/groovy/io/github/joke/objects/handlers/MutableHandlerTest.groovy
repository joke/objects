package io.github.joke.objects.handlers

import com.squareup.javapoet.MethodSpec
import io.github.joke.objects.generator.GettersGenerator
import io.github.joke.objects.generator.SettersGenerator
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import static io.github.joke.objects.Target.MUTABLE
import static io.github.joke.objects.handlers.MutableHandler.Module.provideGetters
import static io.github.joke.objects.handlers.MutableHandler.Module.provideProcessorTarget
import static io.github.joke.objects.handlers.MutableHandler.Module.provideSetters

@Mockable(MethodSpec)
@Subject(MutableHandler)
class MutableHandlerTest extends Specification {

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
