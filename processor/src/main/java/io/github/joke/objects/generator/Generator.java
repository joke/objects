package io.github.joke.objects.generator;

import javax.lang.model.element.TypeElement;

public interface Generator<T> {

    T generate(TypeElement element);

}
