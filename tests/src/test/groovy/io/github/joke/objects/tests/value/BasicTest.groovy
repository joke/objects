package io.github.joke.objects.tests.value

import io.github.joke.objects.tests.value.PersonImpl
import spock.lang.Specification

import static org.junit.platform.commons.util.ReflectionUtils.isAbstract
import static org.junit.platform.commons.util.ReflectionUtils.isNotFinal
import static org.junit.platform.commons.util.ReflectionUtils.isNotStatic
import static org.junit.platform.commons.util.ReflectionUtils.isPublic

class BasicTest extends Specification {

    def 'implementation is public'() {
        expect:
        isPublic PersonImpl
    }

    def 'implementation is not final'() {
        expect:
        isNotFinal PersonImpl
    }

    def 'implementation is not static'() {
        expect:
        isNotStatic PersonImpl
    }

    def 'implementation is not abstract'() {
        expect:
        !isAbstract(PersonImpl)
    }

}
