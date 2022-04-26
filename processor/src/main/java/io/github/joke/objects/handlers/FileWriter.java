package io.github.joke.objects.handlers;

import com.squareup.javapoet.JavaFile;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.inject.Inject;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;

@NotNull
public class FileWriter {

    private final Filer filer;
    private final Messager messager;
    private final Set<JavaFile> javaFiles;
    private final TypeElement typeElement;

    @Inject
    public FileWriter(final Filer filer, final Messager messager, final Set<JavaFile> javaFiles, final TypeElement typeElement) {
        this.filer = filer;
        this.messager = messager;
        this.javaFiles = javaFiles;
        this.typeElement = typeElement;
    }

    void write() {
        javaFiles.forEach(this::writeAndLog);
    }

    private void writeAndLog(final JavaFile javaFile) {
        try {
            javaFile.writeTo(filer);
        } catch (final IOException e) {
            messager.printMessage(ERROR, "Unable to create file: " + javaFile.toJavaFileObject().getName(), typeElement);
        }
    }

}
