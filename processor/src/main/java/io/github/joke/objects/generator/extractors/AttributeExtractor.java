package io.github.joke.objects.generator.extractors;

import io.github.joke.objects.scopes.TypeElementScope;
import org.jetbrains.annotations.VisibleForTesting;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.beans.Introspector.decapitalize;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.util.ElementFilter.methodsIn;

@TypeElementScope
public class AttributeExtractor {

    private final Elements elements;
    private final TypeElement typeElement;

    private final AnnotationExtractor annotationExtractor;

    @Inject
    public AttributeExtractor(final Elements elements, final TypeElement typeElement, final AnnotationExtractor annotationExtractor) {
        this.elements = elements;
        this.typeElement = typeElement;
        this.annotationExtractor = annotationExtractor;
    }

    public List<Attribute> getAttributes() {
        return methodsIn(elements.getAllMembers(typeElement)).stream()
                .filter(executableElement -> executableElement.getModifiers().contains(ABSTRACT))
                .filter(executableElement -> !executableElement.getModifiers().contains(PRIVATE))
                .filter(executableElement -> !executableElement.getModifiers().contains(FINAL))
                .filter(executableElement -> !executableElement.getModifiers().contains(STATIC))
                .filter(executableElement -> executableElement.getParameters().isEmpty())
                .filter(executableElement -> executableElement.getReturnType().getKind() != TypeKind.VOID)
                .map(this::buildAttribute)
                .collect(toList());
    }

    @VisibleForTesting
    protected Attribute buildAttribute(final ExecutableElement element) {
        return new Attribute(
                element, determinePropertyName(element),
                element.getReturnType(),
                annotationExtractor.getNullableAnnotations(element),
                annotationExtractor.getNotNullAnnotations(element),
                annotationExtractor.getAnnotations(element)
        );
    }

    protected static String determinePropertyName(final ExecutableElement origin) {
        return decapitalize(origin.getSimpleName().toString().replaceFirst("^(get|is)", ""));
    }
}
