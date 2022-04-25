package io.github.joke.objects.tests.mutable


import spock.lang.Specification

import static java.lang.reflect.Modifier.isFinal
import static org.junit.platform.commons.util.ReflectionUtils.findConstructors
import static org.junit.platform.commons.util.ReflectionUtils.isPublic

class ConstructorTest extends Specification {

    def 'one constructor is generated'() {
        setup:
        def res = findConstructors(PersonImpl, {true})

        expect:
        res.size() == 1
    }

    def 'is public'() {
        setup:
        def res = findConstructors(PersonImpl, {true})

        expect:
        isPublic res[0]
    }

    def 'has all parameters'() {
        setup:
        def res = findConstructors(PersonImpl, {true})

        expect:
        verifyAll(res[0]) {
            parameterCount == 2
            verifyAll(parameters[0]) {
                name == 'name'
                isFinal modifiers
                parameterizedType == String
            }
            verifyAll(parameters[1]) {
                name == 'parents'
                isFinal modifiers
                parameterizedType.typeName == 'java.util.List<io.github.joke.objects.tests.mutable.Person>'
            }
        }
    }

    def 'constructor sets properties'() {
        setup:
        Person parent = Mock()

        def person = new PersonImpl('John Doe', [parent])

        expect:
        verifyAll(person) {
            name == 'John Doe'
            parents == [parent]
        }
    }

}
