package io.github.joke.objects.generator.extractors;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

@SuppressWarnings("NullAway")
public class Attribute {

    private final String name;
    private final ExecutableElement origin;
    private final TypeMirror type;
    private final List<? extends AnnotationMirror> nullableAnnotations;
    private final List<? extends AnnotationMirror> notNullAnnotations;
    private final List<? extends AnnotationMirror> annotations;

    public Attribute(
            final ExecutableElement origin,
            final String name,
            final TypeMirror type,
            final List<? extends AnnotationMirror> nullableAnnotations,
            final List<? extends AnnotationMirror> notNullAnnotations,
            final List<? extends AnnotationMirror> annotations) {
        this.name = name;
        this.origin = origin;
        this.type = type;
        this.nullableAnnotations = nullableAnnotations;
        this.notNullAnnotations = notNullAnnotations;
        this.annotations = annotations;
    }

    public ExecutableElement getOrigin() {
        return origin;
    }

    public String getName() {
        return name;
    }

    public TypeMirror getType() {
        return type;
    }

    public boolean isNotNull() {
        return !notNullAnnotations.isEmpty();
    }

    public boolean isNullable() {
        return !nullableAnnotations.isEmpty();
    }

    public List<? extends AnnotationMirror> getAnnotations() {
        return annotations;
    }
}
