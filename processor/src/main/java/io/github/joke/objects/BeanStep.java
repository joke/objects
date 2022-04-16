package io.github.joke.objects;

import io.github.joke.objects.processors.ElementProcessor;
import io.github.joke.objects.processors.BeanProcessor;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;

public class BeanStep extends AbstractStep<Bean, TypeElement> {

    final ElementProcessor processor;

    public BeanStep(final Filer filer) {
        super(filer);
        processor = new BeanProcessor(filer);
    }

    @Override
    protected boolean processInternal(final TypeElement element) {
        return processor.process(element);
    }

}
