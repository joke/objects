package io.github.joke.objects.handlers;

import javax.lang.model.element.TypeElement;

public interface Handler {

    void process(TypeElement typeElement);

}
