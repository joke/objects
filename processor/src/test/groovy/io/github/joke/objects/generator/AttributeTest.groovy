package io.github.joke.objects.generator

import io.github.joke.objects.generator.extractors.Attribute
import spock.lang.Specification
import spock.lang.Subject

import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.ExecutableElement
import javax.lang.model.type.TypeMirror

class AttributeTest extends Specification {

    TypeMirror type = Mock()
    ExecutableElement origin = Mock()
    AnnotationMirror annotation = Mock()
    AnnotationMirror notNullAnnotation = Mock()
    AnnotationMirror nullableAnnotation = Mock()

    @Subject
    def attribute = new Attribute(origin, 'my-prop', type, [nullableAnnotation], [notNullAnnotation], [annotation])

    def 'get name'() {
        expect:
        attribute.name == 'my-prop'
    }

    def 'get origin'() {
        expect:
        attribute.origin == origin
    }

    def 'get type'() {
        expect:
        attribute.type == type
    }

    def 'is nullable'() {
        expect:
        attribute.nullable
    }

    def 'is not null'() {
        expect:
        attribute.notNull
    }

    def 'get annotations'() {
        expect:
        attribute.annotations ==~ [annotation]
    }
}
