package io.github.joke.objects.processors;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;

public abstract class ThrowingProcessor extends ElementProcessor {

    protected ThrowingProcessor(final Filer filer) {
        super(filer);
    }

    final public boolean process(final TypeElement element) {
        processInternal(element);
        return true;
    }

    protected abstract void processInternal(TypeElement element);

}
