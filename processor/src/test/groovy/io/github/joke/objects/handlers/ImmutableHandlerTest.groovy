package io.github.joke.objects.handlers

import com.squareup.javapoet.MethodSpec
import io.github.joke.objects.generator.GettersGenerator
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import static io.github.joke.objects.Target.IMMUTABLE
import static io.github.joke.objects.handlers.ImmutableHandler.Module.provideGetters
import static io.github.joke.objects.handlers.ImmutableHandler.Module.provideProcessorTarget

@Mockable(MethodSpec)
@Subject(ImmutableHandler)
class ImmutableHandlerTest extends Specification {

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
