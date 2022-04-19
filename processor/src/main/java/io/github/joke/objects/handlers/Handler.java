package io.github.joke.objects.handlers;

import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;

@NotNull
public interface Handler {

    void process(TypeElement typeElement);

}
