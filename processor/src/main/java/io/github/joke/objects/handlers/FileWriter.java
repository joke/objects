package io.github.joke.objects.handlers;

import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Filer;
import javax.inject.Inject;

public class FileWriter {

    private final Filer filer;
    private final JavaFile javaFile;

    @Inject
    public FileWriter(final Filer filer, final JavaFile javaFile) {
        this.javaFile = javaFile;
        this.filer = filer;
    }

    void write() {
        try {
            javaFile.writeTo(filer);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
