package io.github.joke.objects.tests

import io.github.joke.objects.tests.basic.PersonImpl
import spock.lang.Specification

import static org.junit.platform.commons.util.ReflectionUtils.findConstructors
import static org.junit.platform.commons.util.ReflectionUtils.isAbstract
import static org.junit.platform.commons.util.ReflectionUtils.isNotFinal
import static org.junit.platform.commons.util.ReflectionUtils.isNotStatic
import static org.junit.platform.commons.util.ReflectionUtils.isPublic

class BasicBeanTest extends Specification {

    def 'implementation class is created'() {
        expect:
        new PersonImpl()
    }

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

    def 'implementation has default constructor'() {
        setup:
        def constructors = findConstructors(PersonImpl, {true})

        expect:
        constructors.size() == 1
    }

}
