package io.github.joke.objects.tests.immutable

import spock.lang.Specification

import static org.junit.platform.commons.util.ReflectionUtils.HierarchyTraversalMode.TOP_DOWN
import static org.junit.platform.commons.util.ReflectionUtils.findFields
import static org.junit.platform.commons.util.ReflectionUtils.isNotFinal
import static org.junit.platform.commons.util.ReflectionUtils.isNotStatic
import static org.junit.platform.commons.util.ReflectionUtils.isPrivate

class FieldTest extends Specification {

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
