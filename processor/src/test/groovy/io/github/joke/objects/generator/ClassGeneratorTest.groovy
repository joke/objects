package io.github.joke.objects.generator

import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import spock.lang.Specification
import spock.lang.Subject

import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType

import static javax.lang.model.element.ElementKind.CLASS
import static javax.lang.model.element.ElementKind.INTERFACE
import static javax.lang.model.element.Modifier.PUBLIC

class ClassGeneratorTest extends Specification {

    TypeElement element = DeepMock()
    AnnotationSpec annotationSpec = DeepMock()

    @Subject
    ClassGenerator classGenerator = Spy(new ClassGenerator(element, annotationSpec))

    def 'get type builder for interface'() {
        setup:
        TypeElement element1 = Mock()
        DeclaredType declaredType = Mock()

        when:
        def res = classGenerator.typeBuilder

        then:
        1 * classGenerator.determineClassName() >> ClassName.get(String)
        1 * element.kind  >> INTERFACE
        1 * element.asType() >> declaredType
        1 * declaredType.accept(_, _) >> ClassName.get(Set)
        1 * declaredType.asElement() >> element1
        1 * element1.enclosedElements >> []
        1 * element1.interfaces >> []

        verifyAll(res) {
            superinterfaces == [ ClassName.get(Set) ]
            modifiers == [PUBLIC]
            annotations == [annotationSpec]
            name == 'String'
        }
    }

    def 'get type builder for abstract class'() {
        setup:
        TypeElement element1 = Mock()
        DeclaredType declaredType = Mock()

        when:
        def res = classGenerator.typeBuilder

        then:
        1 * classGenerator.determineClassName() >> ClassName.get(String)
        1 * element.kind  >> CLASS
        1 * element.asType() >> declaredType
        1 * declaredType.accept(_, _) >> ClassName.get(Set)
        1 * declaredType.asElement() >> element1
        1 * element1.enclosedElements >> []
        1 * element1.interfaces >> []

        verifyAll(res) {
            superclass == ClassName.get(Set)
            modifiers == [PUBLIC]
            annotations == [annotationSpec]
            name == 'String'
        }
    }
}
