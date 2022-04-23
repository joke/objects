package io.github.joke.objects.tests.bean

import org.junit.platform.commons.util.ReflectionUtils
import spock.lang.Specification

import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN
import static org.junit.platform.commons.util.ReflectionUtils.findFields
import static org.junit.platform.commons.util.ReflectionUtils.findMethods
import static org.junit.platform.commons.util.ReflectionUtils.isNotFinal
import static org.junit.platform.commons.util.ReflectionUtils.isNotStatic
import static org.junit.platform.commons.util.ReflectionUtils.isPublic

class SetterTest extends Specification {

    def 'has setter with interface return type'() {
        setup:
        def methods = findMethods(PersonImpl, {it.name == 'setName'})

        expect:
        methods.size() == 1
        verifyAll(methods[0]) {method ->
            isPublic method
            isNotFinal method
            isNotStatic method
            method.returnType == void.class
            verifyAll(method.parameters) {
                size() == 1
                first().name == 'name'
                first().type == String
            }
        }
    }

    def 'setter sets field with interface return type'() {
        setup:
        def person = new PersonImpl(null, null)
        person.parents = [person]

        expect:
        def field = findFields(PersonImpl, {it.name == 'parents'}, TOP_DOWN).first()
        ReflectionUtils.makeAccessible(field)
        field.get(person) == [person]
    }

    def 'has setter'() {
        setup:
        def methods = findMethods(PersonImpl, {it.name == 'setName'})

        expect:
        methods.size() == 1
        verifyAll(methods[0]) {method ->
            isPublic method
            isNotFinal method
            isNotStatic method
            method.returnType == void.class
            verifyAll(method.parameters) {
                size() == 1
                first().name == 'name'
                first().type == String
            }
        }
    }

    def 'setter sets field'() {
        setup:
        def person = new PersonImpl(null, null)
        person.name = 'John Doe'

        expect:
        def field = findFields(PersonImpl, {it.name == 'name'}, TOP_DOWN).first()
        ReflectionUtils.makeAccessible(field)
        field.get(person) == 'John Doe'
    }

}
