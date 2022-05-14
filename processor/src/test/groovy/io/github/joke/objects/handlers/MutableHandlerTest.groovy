package io.github.joke.objects.handlers

import com.squareup.javapoet.MethodSpec
import io.github.joke.objects.generator.SetterGenerator
import io.github.joke.objects.handlers.MutableHandler.MutableAttributeHandler
import io.github.joke.objects.handlers.MutableHandler.MutableAttributeHandler.AttributeModule
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import static io.github.joke.objects.Target.MUTABLE

@Subject(MutableHandler)
class MutableHandlerTest extends Specification {

    @Shared
    MethodSpec methodSpec = DeepMock()

    def 'module provide processor target'() {
        expect:
        MutableHandler.Module.provideProcessorTarget() == MUTABLE
    }

    def 'module provide attribute component factory'() {
        expect:
        MutableHandler.Module.provideAttributeComponentFactory() in MutableAttributeHandler.Factory
    }

    def 'provide setter'() {
        setup:
        SetterGenerator generator = DeepMock()

        when:
        def res = AttributeModule.provideSetter(generator)

        then:
        1 * generator.generate() >> (method as Set)
        0 * _

        expect:
        res ==~ expected

        where:
        method       || expected
        []           || []
        [methodSpec] || [methodSpec]
    }
}
