package io.github.joke.objects.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.github.joke.objects.customize.ToString;
import io.github.joke.objects.customize.ToString.Style;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.scopes.TypeElementScope;
import io.github.joke.objects.utils.AnnotationUtils;

import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import static com.squareup.javapoet.MethodSpec.overriding;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.util.ElementFilter.methodsIn;

@TypeElementScope
public class ToStringGenerator {

    private final Elements elements;
    private final TypeElement typeElement;
    private final List<Attribute> attributes;
    private final Map<String, Object> typeConfig;

    @Inject
    public ToStringGenerator(final Elements elements, final TypeElement typeElement, final List<Attribute> attributes, final AnnotationUtils annotationUtils) {
        this.elements = elements;
        this.typeElement = typeElement;
        this.attributes = attributes;
        typeConfig = annotationUtils.getValuesOrDefaultsFromAnnotation(typeElement, ToString.class);
    }

    public Set<MethodSpec> generate() {
        if (hasToStringImplementation()) {
            return emptySet();
        }

        final ExecutableElement toStringElement = findToStringElement(elements.getAllMembers(typeElement))
                .orElseThrow(RuntimeException::new);
        final MethodSpec toString = overriding(toStringElement)
                .addStatement(selectToStringStyle())
                .build();
        return singleton(toString);
    }

    protected boolean hasToStringImplementation() {
        return findToStringElement(typeElement.getEnclosedElements())
                .filter(executableElement -> !executableElement.getModifiers().contains(ABSTRACT))
                .isPresent();
    }

    private CodeBlock selectToStringStyle() {
        final Style style = requireNonNull((Style) typeConfig.get("style"));
        switch (style) {
            case STRING_JOINER:
                return stringJoiner();
            case TO_STRING_BUILDER:
                return toStringBuilder();
            default:
                throw new UnsupportedOperationException();
        }
    }

    private CodeBlock stringJoiner() {
        final CodeBlock.Builder builder = CodeBlock.builder()
                .add(
                        "return new $T($S, $T.class.getSimpleName() + $S, $S)\n",
                        StringJoiner.class,
                        ", ",
                        typeElement.asType(),
                        "[",
                        "]");
        attributes.forEach(attribute -> builder.add(".add($1S + $1N)\n", attribute.getName()));
        builder.add(".toString()");
        return builder.build();
    }

    private CodeBlock toStringBuilder() {
        final ClassName toStringBuilder = ClassName.get("org.apache.commons.lang3.builder", "ToStringBuilder");
        final CodeBlock.Builder builder = CodeBlock.builder()
                .add("return new $T(this)\n", toStringBuilder);
        attributes.forEach(attribute -> builder.add(".append($1S, $1N)\n", attribute.getName()));
        builder.add(".toString()");
        return builder.build();
    }

    private Optional<ExecutableElement> findToStringElement(final List<? extends Element> elements) {
        return methodsIn(elements).stream()
                .filter(executableElement ->
                        executableElement.getSimpleName().toString().equals("toString"))
                .findFirst();
    }
}
