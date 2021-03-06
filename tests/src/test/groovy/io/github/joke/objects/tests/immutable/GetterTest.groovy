package io.github.joke.objects.tests.immutable

import org.junit.platform.commons.util.ReflectionUtils
import spock.lang.Specification

import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN
import static org.junit.platform.commons.util.ReflectionUtils.findFields
import static org.junit.platform.commons.util.ReflectionUtils.findMethods
import static org.junit.platform.commons.util.ReflectionUtils.isNotFinal
import static org.junit.platform.commons.util.ReflectionUtils.isNotStatic
import static org.junit.platform.commons.util.ReflectionUtils.isPublic

class GetterTest extends Specification {

    def 'has getter'() {
        setup:
        def methods = findMethods(PersonImpl, {it.name == 'getName'})

        expect:
        methods.size() == 1
        verifyAll(methods[0]) {method ->
            isPublic method
            isNotFinal method
            isNotStatic method
            method.parameterCount == 0
            method.returnType == String
        }
    }

    def 'getter returns field'() {
        setup:
        def person = new PersonImpl(null, null)
        def field = findFields(PersonImpl, {it.name == 'name'}, TOP_DOWN).first()
        ReflectionUtils.makeAccessible(field)
        field.set(person, "John Doe")

        expect:
        person.name == 'John Doe'
    }

    def 'has getter with interface return type'() {
        setup:
        def methods = findMethods(PersonImpl, {it.name == 'getParents'})

        expect:
        methods.size() == 1
        verifyAll(methods[0]) { method ->
            isPublic method
            isNotFinal method
            isNotStatic method
            method.parameterCount == 0
            method.returnType == List
        }
    }

}
