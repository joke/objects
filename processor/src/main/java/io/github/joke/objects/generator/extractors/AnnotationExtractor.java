package io.github.joke.objects.generator.extractors;

import org.jetbrains.annotations.VisibleForTesting;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Singleton
public class AnnotationExtractor {

    private final Elements elements;

    @Inject
    public AnnotationExtractor(final Elements elements) {
        this.elements = elements;
    }

    public List<AnnotationMirror> getNullableAnnotations(final Element element) {
        return elements.getAllAnnotationMirrors(element).stream()
                .filter(this::matchesNullableAnnotation)
                .collect(toList());
    }

    public List<AnnotationMirror> getNotNullAnnotations(final Element element) {
        return elements.getAllAnnotationMirrors(element).stream()
                .filter(this::matchesNotNullAnnotation)
                .collect(toList());
    }

    public List<AnnotationMirror> getAnnotations(final Element element) {
        final List<AnnotationMirror> notNullAnnotations = getNotNullAnnotations(element);
        final List<AnnotationMirror> nullableAnnotations = getNullableAnnotations(element);
        final List<AnnotationMirror> annotations = new ArrayList<>(notNullAnnotations.size() + nullableAnnotations.size());
        annotations.addAll(notNullAnnotations);
        annotations.addAll(nullableAnnotations);
        return annotations;
     }

    @VisibleForTesting
    protected boolean matchesNullableAnnotation(final AnnotationMirror annotationMirror) {
        final Name name = annotationMirror.getAnnotationType().asElement().getSimpleName();
        return name.contentEquals("Nullable");
    }

    @VisibleForTesting
    protected boolean matchesNotNullAnnotation(final AnnotationMirror annotationMirror) {
        final Name name = annotationMirror.getAnnotationType().asElement().getSimpleName();
        return name.contentEquals("NotNull") || name.contentEquals("NonNull");
    }

}
