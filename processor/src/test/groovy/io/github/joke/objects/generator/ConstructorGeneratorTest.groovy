package io.github.joke.objects.generator

import com.squareup.javapoet.ClassName
import io.github.joke.objects.scanner.Property
import spock.lang.Specification
import spock.lang.Subject

import javax.lang.model.element.Element

import static com.squareup.javapoet.TypeName.BYTE
import static javax.lang.model.element.Modifier.FINAL
import static javax.lang.model.element.Modifier.PUBLIC

class ConstructorGeneratorTest extends Specification {

    def propertyA = new Property(Mock(Element), 'name', false, BYTE)
    def propertyB = new Property(Mock(Element), 'amount', true, ClassName.get('java.lang', 'Long'))

    @Subject
    def constructorGenerator = new ConstructorGenerator([propertyA, propertyB])

    def 'all args constructor is generated'() {
        setup:
        def constructs = constructorGenerator.constructors

        expect:
        constructs.size() == 1
        PUBLIC in constructs[0].modifiers
    }

    def 'has two parameters'() {
        setup:
        def constructs = constructorGenerator.constructors

        expect:
        verifyAll(constructs[0]) {
            parameters.size() == 2
            verifyAll(parameters[0]) {
                name == 'name'
                FINAL in modifiers
                "$type" == 'byte'
            }
            verifyAll(parameters[1]) {
                name == 'amount'
                FINAL in modifiers
                "$type" == Long.name
            }
        }
    }

    def 'has field assignment'() {
        setup:
        def constructs = constructorGenerator.constructors

        expect:
        verifyAll(constructs[0]) {
            "$code" == '''
            |this.name = name;
            |this.amount = amount;
            |'''.trim().stripMargin()
        }
    }

}
