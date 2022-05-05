package io.github.joke.objects.processor

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.TypeSpec
import io.github.joke.objects.processor.FileWriter
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.Element

import static javax.tools.Diagnostic.Kind.ERROR

@Mockable([JavaFile, TypeSpec])
class FileWriterTest extends Specification {

    Filer filer = DeepMock()
    Messager messager = DeepMock()

    @Subject
    def fileWriter = new FileWriter(filer, messager)


    def 'write file'() {
        setup:
        JavaFile javaFile = DeepMock()

        when:
        fileWriter.write(javaFile)

        then:
        1 * javaFile.writeTo(filer) >> {}
        0 * _
    }

    def 'log message on exception'() {
        setup:
        Element element = DeepMock()
        def typeSpec = TypeSpec.enumBuilder('MyEnum')
                .addEnumConstant('ONE')
                .addOriginatingElement(element)
                .build()
        def javaFile = JavaFile.builder('asdf', typeSpec).build()

        when:
        fileWriter.write(javaFile)

        then:
        1 * filer.createSourceFile(_, _) >> { throw new IOException() }
        1 * messager.printMessage(ERROR, 'Unable to create file: asdf/MyEnum.java', element) >> {}
    }
}
