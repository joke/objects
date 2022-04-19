package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.generator.GeneratorUtils.determinePropertyName;
import static io.github.joke.objects.generator.GeneratorUtils.filterGetters;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.util.ElementFilter.methodsIn;

public class PropertiesGenerator {

    private final TypeElement element;

    @Inject
    public PropertiesGenerator(final TypeElement element) {
        this.element = element;
    }

    public Set<FieldSpec> getProperties() {
        final List<ExecutableElement> methods = methodsIn(element.getEnclosedElements());

        return filterGetters(methods).stream()
                .map(this::buildProperties)
                .collect(toSet());
    }

    private FieldSpec buildProperties(final ExecutableElement element) {
        final String propertyName = determinePropertyName(element);
        final TypeName type = TypeName.get(element.getReturnType());
        return FieldSpec.builder(type, propertyName, PRIVATE)
                .build();
    }

}