package io.github.joke.objects.generator.scanner;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import java.util.StringJoiner;

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

    @Override
    public String toString() {
        return new StringJoiner(", ", Property.class.getSimpleName() + "[", "]")
                .add("origin=" + origin)
                .add("name='" + name + "'")
                .add("isSynthetic=" + isSynthetic)
                .add("type=" + type)
                .toString();
    }
}
