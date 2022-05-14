package io.github.joke.objects.processor;

import com.squareup.javapoet.JavaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.Element;
import java.io.IOException;

import static javax.tools.Diagnostic.Kind.ERROR;

@NotNull
@Singleton
class FileWriter {

    private final Filer filer;
    private final Messager messager;

    @Inject
    FileWriter(final Filer filer, final Messager messager) {
        this.filer = filer;
        this.messager = messager;
    }

    void write(final JavaFile javaFile) {
        try {
            javaFile.writeTo(filer);
        } catch (final IOException e) {
            @Nullable
            final Element element = javaFile.typeSpec.originatingElements.stream()
                    .findFirst()
                    .orElse(null);
            messager.printMessage(ERROR, "Unable to create file: " + javaFile.toJavaFileObject().getName(), element);
        }
    }

}
