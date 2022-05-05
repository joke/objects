package io.github.joke.objects.generator.scanner

import com.squareup.javapoet.TypeName
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.Messager
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

import static javax.lang.model.element.ElementKind.CLASS
import static javax.lang.model.element.ElementKind.FIELD
import static javax.lang.model.element.ElementKind.METHOD
import static javax.lang.model.element.Modifier.ABSTRACT
import static javax.lang.model.element.Modifier.PRIVATE
import static javax.lang.model.element.Modifier.PROTECTED
import static javax.lang.model.element.Modifier.STATIC
import static javax.lang.model.type.TypeKind.DOUBLE
import static javax.lang.model.type.TypeKind.VOID
import static javax.tools.Diagnostic.Kind.ERROR
import static org.apache.commons.lang3.reflect.FieldUtils.readField

class PropertyScannerTest extends Specification {

    Messager messager = DeepMock()
    TypeElement element = DeepMock()

    @Subject
    def propertyScanner = Spy(new PropertyScanner(messager, element))

    def 'check constructor'() {
        expect:
        readField(propertyScanner, 'messager', true) == messager
        readField(propertyScanner, 'element', true) == element
    }

    def 'get properties'() {
        setup:
        def properties = []

        when:
        def res = propertyScanner.properties

        then:
        1 * propertyScanner.determineProperties() >> properties
        1 * propertyScanner.checkForDuplicates({ it.is properties }) >> {}
        1 * propertyScanner._
        0 * _

        expect:
        res.is properties
    }

    def 'determine properties'() {
        setup:
        Element element1 = DeepMock()
        Element element2 = DeepMock()
        Property property = DeepMock()

        when:
        def res = propertyScanner.determineProperties()

        then:
        1 * element.enclosedElements >> [element1, element2]
        1 * propertyScanner.buildPropertyInfo(element1) >> null
        1 * propertyScanner.buildPropertyInfo(element2) >> property
        1 * propertyScanner._
        0 * _

        expect:
        res == [property]
    }

    def 'check for duplicates'() {
        setup:
        Property propertyA = Stub { name >> 'a' }
        Property propertyB = Stub { name >> 'b' }
        Property propertyC = Stub { name >> 'b' }

        when:
        propertyScanner.checkForDuplicates([propertyA, propertyB, propertyC])

        then:
        propertyC.origin >> element
        1 * messager.printMessage(ERROR, 'Trying to generate a property with the same name. The property might already exist with a base class or it would be generated from an property method.', element) >> {}
        1 * propertyScanner._
        0 * _
    }

    def 'build property info'() {
        setup:
        def elementType = DeepMock(type)

        when:
        def res = propertyScanner.buildPropertyInfo(elementType)

        then:
        invokations * propertyScanner."$method"({ it in type }) >> result
        1 * elementType.kind >> kind
        1 * propertyScanner._
        0 * _

        expect:
        res == result

        where:
        type              | kind   | method       || invokations | result
        VariableElement   | FIELD  | "fromField"  || 1           | DeepMock(Property)
        ExecutableElement | METHOD | "fromMethod" || 1           | DeepMock(Property)
        TypeElement       | CLASS  | ''           || 0           | null
    }

    def 'from field'() {
        setup:
        VariableElement variableElement = DeepMock()
        TypeMirror typeMirror = DeepMock()

        when:
        def res = propertyScanner.fromField(variableElement)

        then:
        1 * variableElement.modifiers >> [PROTECTED]
        1 * variableElement.simpleName.toString() >> 'my-var'
        1 * variableElement.asType() >> typeMirror
        1 * typeMirror.accept(_, _) >> TypeName.DOUBLE
        1 * propertyScanner._

        expect:
        verifyAll(res) {
            origin == variableElement
            name == 'my-var'
            type == TypeName.DOUBLE
            !synthetic
        }
    }

    def 'from private field'() {
        setup:
        VariableElement variableElement = DeepMock()

        when:
        def res = propertyScanner.fromField(variableElement)

        then:
        1 * variableElement.modifiers >> [PRIVATE]
        1 * propertyScanner._
        0 * _

        expect:
        res == null
    }

    def 'is excluded from property generation'() {
        setup:
        ExecutableElement executableElement = DeepMock()

        when:
        def res = propertyScanner.isExcludedFromPropertyGeneration(executableElement)

        then:
        executableElement.modifiers >> modifiers
        executableElement.varArgs >> isVarArgs
        executableElement.returnType.kind >> kind
        executableElement.parameters >> parameters
        1 * propertyScanner._

        expect:
        res == excluded

        where:
        modifiers          | isVarArgs | kind   | parameters                  || excluded
        [STATIC]           | false     | DOUBLE | []                          || true
        []                 | true      | DOUBLE | []                          || true
        []                 | false     | VOID   | []                          || true
        []                 | false     | DOUBLE | [DeepMock(VariableElement)] || true
        []                 | false     | DOUBLE | []                          || true
        [STATIC, ABSTRACT] | false     | DOUBLE | []                          || true
        [ABSTRACT]         | true      | DOUBLE | []                          || true
        [ABSTRACT]         | false     | VOID   | []                          || true
        [ABSTRACT]         | false     | DOUBLE | [DeepMock(VariableElement)] || true
        [ABSTRACT]         | false     | DOUBLE | []                          || false
    }

    def 'from method'() {
        setup:
        TypeMirror typeMirror = DeepMock()
        ExecutableElement executableElement = DeepMock()

        when:
        def res = propertyScanner.fromMethod(executableElement)

        then:
        1 * propertyScanner.isExcludedFromPropertyGeneration(executableElement) >> false
        1 * propertyScanner.determinePropertyName(executableElement) >> 'my-var'
        executableElement.returnType >> typeMirror
        1 * typeMirror.accept(_, _) >> TypeName.DOUBLE
        1 * propertyScanner._

        expect:
        verifyAll(res) {
            origin == executableElement
            name == 'my-var'
            type == TypeName.DOUBLE
            synthetic
        }
    }

    def 'from excluded method'() {
        setup:
        ExecutableElement executableElement = DeepMock()

        when:
        def res = propertyScanner.fromMethod(executableElement)

        then:
        1 * propertyScanner.isExcludedFromPropertyGeneration(executableElement) >> true
        1 * propertyScanner._

        expect:
        res == null
    }

    def 'determine property name'() {
        setup:
        ExecutableElement executableElement = DeepMock()

        when:
        def res = propertyScanner.determinePropertyName(executableElement)

        then:
        executableElement.simpleName.toString() >> name

        expect:
        res == expected

        where:
        name             || expected
        'calculatedSize' || 'calculatedSize'
        'isValid'        || 'valid'
        'getName'        || 'name'
        'GetName'        || 'GetName'
        'IsValid'        || 'IsValid'
    }
}
