package io.github.joke.objects.handlers

import io.github.joke.objects.handlers.ImmutableHandler.ImmutableAttributeHandler
import spock.lang.Specification
import spock.lang.Subject

import static io.github.joke.objects.Target.IMMUTABLE

@Subject(ImmutableHandler)
class ImmutableHandlerTest extends Specification {

    def 'module provide getters'() {
        expect:
        ImmutableHandler.Module.provideProcessorTarget() == IMMUTABLE
    }

    def 'module provide attribute component factory'() {
        expect:
        ImmutableHandler.Module.provideAttributeComponentFactory() in ImmutableAttributeHandler.Factory
    }
}
