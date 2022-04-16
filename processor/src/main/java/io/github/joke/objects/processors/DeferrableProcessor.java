package io.github.joke.objects.processors;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;

public abstract class DeferrableProcessor extends ElementProcessor {

    public DeferrableProcessor(final Filer filer) {
        super(filer);
    }

    final public boolean process(final TypeElement element) {
        try {
            writeOutput(element);
        } catch (final Exception e) {
            return false;
        }
        return true;
    }

}
