package io.github.joke.objects.handlers

import com.squareup.javapoet.JavaFile
import io.github.joke.spockmockable.Mockable
import spock.lang.Specification
import spock.lang.Subject

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.TypeElement

import static javax.tools.Diagnostic.Kind.ERROR

@Mockable(JavaFile)
class FileWriterTest extends Specification {

    Filer filer = DeepMock()
    JavaFile javaFile = DeepMock()
    Messager messager = DeepMock()
    TypeElement typeElement = DeepMock()

    @Subject
    def fileWriter = new FileWriter(filer, messager, [javaFile] as Set, typeElement)

    def 'write file'() {
        when:
        fileWriter.write()

        then:
        1 * javaFile.writeTo(filer) >> {}
        0 * _
    }

    def 'log message on exception'() {
        when:
        fileWriter.write()

        then:
        1 * javaFile.writeTo(filer) >> { throw new IOException() }
        1 * javaFile.toJavaFileObject().name >> 'my-name'
        _ * javaFile.toJavaFileObject()
        1 * messager.printMessage(ERROR, 'Unable to create file: my-name', typeElement) >> {}
        0 * _
    }
}
