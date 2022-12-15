package io.github.joke.objects.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor8;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toMap;
import static javax.lang.model.util.ElementFilter.methodsIn;

@Singleton
public class AnnotationUtils {

    private final Types types;
    private final Elements elements;
    private final Map<TypeElement, Map<String, Object>> cachedFallbacks = new ConcurrentHashMap<>();

    @Inject
    public AnnotationUtils(final Types types, final Elements elements) {
        this.types = types;
        this.elements = elements;
    }

    public Map<String, Object> getValuesOrDefaultsFromAnnotation(final Element element, final Class<? extends Annotation> annotation) {
        final TypeElement annotationElement = elements.getTypeElement(annotation.getCanonicalName());
        return elements.getAllAnnotationMirrors(element).stream()
                .filter(annotationMirror -> annotationMirror.getAnnotationType().asElement().equals(annotationElement))
                .findFirst()
                .map(this::extractDefaultsFromAnnotation)
                .orElseGet(() -> cacheOrExtractDefaultFromAnnotationClass(annotationElement));
    }

    @NotNull
    private Map<String, Object> cacheOrExtractDefaultFromAnnotationClass(final TypeElement annotationElement) {
        return cachedFallbacks.computeIfAbsent(annotationElement, AnnotationUtils::extractDefaultFromAnnotationClass);
    }

    private static Map<String, Object> extractDefaultFromAnnotationClass(final TypeElement key) {
        return methodsIn(key.getEnclosedElements()).stream()
                .collect(collectingAndThen(toMap(
                                executableElement -> executableElement.getSimpleName().toString(),
                                ExecutableElement::getDefaultValue),
                        Collections::unmodifiableMap));
    }

    @NotNull
    private Map<String, Object> extractDefaultsFromAnnotation(final AnnotationMirror annotationMirror) {
        return elements.getElementValuesWithDefaults(annotationMirror).entrySet().stream()
                .collect(collectingAndThen(toMap(
                                entry -> entry.getKey().getSimpleName().toString(),
                                entry -> new AnnotationValueVisitor().visit(entry.getValue())),
                        Collections::unmodifiableMap));
    }

    private class AnnotationValueVisitor extends SimpleAnnotationValueVisitor8<Object, Void> {

        @Nullable
        @Override
        public Object defaultAction(final Object o, final Void unused) {
            return o;
        }

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Object visitEnumConstant(final VariableElement constant, final Void unused) {
            final String enumValue = constant.getSimpleName().toString();
            final String enumClassName = elements.getBinaryName((TypeElement) types.asElement(constant.asType())).toString();
            try {
                return Enum.valueOf((Class) Class.forName(enumClassName), enumValue);
            } catch (final ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
