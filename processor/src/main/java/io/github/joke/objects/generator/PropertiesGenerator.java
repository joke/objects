package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static io.github.joke.objects.generator.GeneratorUtils.determinePropertyName;
import static io.github.joke.objects.generator.GeneratorUtils.filterGetters;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.util.ElementFilter.methodsIn;

@NotNull
public class PropertiesGenerator {

    private final TypeElement element;

    @Inject
    public PropertiesGenerator(final TypeElement element) {
        this.element = element;
    }

    public List<FieldSpec> getProperties() {
        final List<ExecutableElement> methods = methodsIn(element.getEnclosedElements());
        return filterGetters(methods).stream()
                .map(this::buildProperties)
                .collect(toList());
    }

    private FieldSpec buildProperties(final ExecutableElement element) {
        final String propertyName = determinePropertyName(element);
        final TypeName type = TypeName.get(element.getReturnType());
        return FieldSpec.builder(type, propertyName, PRIVATE)
                .build();
    }

}
