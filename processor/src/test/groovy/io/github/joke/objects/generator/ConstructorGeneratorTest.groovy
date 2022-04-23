package io.github.joke.objects.generator

import com.squareup.javapoet.FieldSpec
import spock.lang.Specification
import spock.lang.Subject

import static javax.lang.model.element.Modifier.FINAL
import static javax.lang.model.element.Modifier.PRIVATE
import static javax.lang.model.element.Modifier.PROTECTED
import static javax.lang.model.element.Modifier.PUBLIC

class ConstructorGeneratorTest extends Specification {

    FieldSpec fieldA = FieldSpec.builder(String, 'name', PROTECTED).build()
    FieldSpec fieldB = FieldSpec.builder(Long, 'amount', PRIVATE).build()

    @Subject
    def constructorGenerator = new ConstructorGenerator([fieldA, fieldB])

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
                "$type" == String.name
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
