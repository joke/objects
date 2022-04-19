package io.github.joke.objects.generator

import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement

import static javax.lang.model.SourceVersion.RELEASE_10
import static javax.lang.model.SourceVersion.RELEASE_7
import static javax.lang.model.SourceVersion.RELEASE_8
import static javax.lang.model.SourceVersion.RELEASE_9

class ClassGeneratorTest extends Specification {

    TypeElement element = DeepMock()
    ProcessingEnvironment processingEnvironment = DeepMock()

    @Subject
    ClassGenerator classGenerator = Spy(new ClassGenerator(element, processingEnvironment))

    def 'generated annotation for is java version dependent'() {
        when:
        def res = classGenerator.generatedAnnotation

        then:
        1 * processingEnvironment.sourceVersion >> javaVersion
        1 * classGenerator._
        0 * _

        expect:
        verifyAll(res) {
            "$type" == expectedType
            members.size() == 1
            "${members.value[0]}" == '"io.github.joke.objects.generator.PropertiesGenerator"'
        }

        where:
        javaVersion || expectedType
        RELEASE_7   || 'javax.annotation.Generated'
        RELEASE_8   || 'javax.annotation.Generated'
        RELEASE_9   || 'javax.annotation.processing.Generated'
        RELEASE_10  || 'javax.annotation.processing.Generated'
    }

    def 'generated annotation for is java version dependent'() {
        when:
        def res = classGenerator.generatedAnnotation

        then:
        1 * processingEnvironment.sourceVersion >> javaVersion
        1 * classGenerator._
        0 * _

        expect:
        verifyAll(res) {
            "$type" == expectedType
            members.size() == 1
            "${members.value[0]}" == '"io.github.joke.objects.generator.PropertiesGenerator"'
        }

        where:
        javaVersion || expectedType
        RELEASE_7   || 'javax.annotation.Generated'
        RELEASE_8   || 'javax.annotation.Generated'
        RELEASE_9   || 'javax.annotation.processing.Generated'
        RELEASE_10  || 'javax.annotation.processing.Generated'
    }

}
