package io.github.joke.objects.processors;

import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.io.IOException;

public abstract class ElementProcessor {

    private final Filer filer;

    protected ElementProcessor(final Filer filer) {
        this.filer = filer;
    }

    public abstract boolean process(TypeElement element);

    abstract protected JavaFile output(TypeElement element);

    protected final void writeOutput(final TypeElement element) throws IOException {
        output(element).writeTo(filer);
    }

}
