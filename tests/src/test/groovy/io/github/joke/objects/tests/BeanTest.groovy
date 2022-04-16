package io.github.joke.objects.tests

import io.github.joke.objects.tests.basic.PersonImpl
import org.junit.platform.commons.util.ReflectionUtils
import spock.lang.Specification

import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN
import static org.junit.platform.commons.util.ReflectionUtils.findConstructors
import static org.junit.platform.commons.util.ReflectionUtils.findFields
import static org.junit.platform.commons.util.ReflectionUtils.findMethods
import static org.junit.platform.commons.util.ReflectionUtils.isAbstract
import static org.junit.platform.commons.util.ReflectionUtils.isNotFinal
import static org.junit.platform.commons.util.ReflectionUtils.isNotStatic
import static org.junit.platform.commons.util.ReflectionUtils.isPrivate
import static org.junit.platform.commons.util.ReflectionUtils.isPublic

class BeanTest extends Specification {

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
        def person = new PersonImpl()
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

    def 'field attributes'() {
        setup:
        def field = findFields(PersonImpl, {it.name == 'name'}, TOP_DOWN).first()

        expect:
        isPrivate field
        isNotFinal field
        isNotStatic field
    }

    def 'field with interface type'() {
        setup:
        def field = findFields(PersonImpl, {it.name == 'parents'}, TOP_DOWN).first()

        expect:
        isPrivate field
        isNotFinal field
        isNotStatic field
    }

}
