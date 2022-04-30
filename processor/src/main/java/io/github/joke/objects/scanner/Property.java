package io.github.joke.objects.scanner;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;

public class Property {

    private final Element origin;
    private final String name;
    private final boolean isSynthetic;
    private final TypeName type;

    public Property(final Element origin, final String name, final boolean isSynthetic, final TypeName type) {
        this.origin = origin;
        this.name = name;
        this.isSynthetic = isSynthetic;
        this.type = type;
    }

    public Element getOrigin() {
        return origin;
    }

    public String getName() {
        return name;
    }

    public boolean isSynthetic() {
        return isSynthetic;
    }

    public TypeName getType() {
        return type;
    }
}
