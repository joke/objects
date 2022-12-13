package io.github.joke.objects.generator

import com.squareup.javapoet.AnnotationSpec
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.ProcessingEnvironment

import static javax.lang.model.SourceVersion.RELEASE_10
import static javax.lang.model.SourceVersion.RELEASE_7
import static javax.lang.model.SourceVersion.RELEASE_8
import static javax.lang.model.SourceVersion.RELEASE_9

class GeneratedAnnotationGeneratorTest extends Specification {

    ProcessingEnvironment processingEnvironment = Mock()

    @Subject
    def generatedAnnotationGenerator = new GeneratedAnnotationGenerator(processingEnvironment)

    def 'generated annotation for is java version dependent'() {
        when:
        def res = generatedAnnotationGenerator.generate()

        then:
        1 * processingEnvironment.sourceVersion >> javaVersion
        _ * AnnotationSpec._
        0 * _

        expect:
        verifyAll(res) {
            "$type" == expectedType
            members.size() == 1
            "${members.value[0]}" == '"io.github.joke.objects.AnnotationProcessor"'
        }

        where:
        javaVersion || expectedType
        RELEASE_7   || 'javax.annotation.Generated'
        RELEASE_8   || 'javax.annotation.Generated'
        RELEASE_9   || 'javax.annotation.processing.Generated'
        RELEASE_10  || 'javax.annotation.processing.Generated'
    }
}
