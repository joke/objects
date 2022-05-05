package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;
import io.github.joke.objects.handlers.ElementScope;
import io.github.joke.objects.generator.scanner.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static com.squareup.javapoet.MethodSpec.overriding;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

@NotNull
@ElementScope
public class GettersGenerator {

    private final List<Property> properties;

    @Inject
    public GettersGenerator(final List<Property> properties) {
        this.properties = properties;
    }

    public Set<MethodSpec> getGetters() {
        return properties.stream()
                .map(this::buildGetter)
                .collect(toSet());
    }

    private MethodSpec buildGetter(final Property property) {
        return createMethodSpec(property)
                .addStatement("return $L", property.getName())
                .build();
    }

    private MethodSpec.Builder createMethodSpec(final Property property) {
        if (property.isSynthetic()) {
            return fromMethod(property);
        }
        else return fromProperty(property);
    }

    private MethodSpec.Builder fromProperty(final Property property) {
        final String getterName = determineGetterName(property.getName());
        return MethodSpec.methodBuilder(getterName)
                .returns(property.getType())
                .addModifiers(PUBLIC);
    }

    private MethodSpec.Builder fromMethod(final Property property) {
        return overriding((ExecutableElement) property.getOrigin());
    }

    public static String determineGetterName(final String propertyName) {
        return "get" + capitalize(propertyName);
    }

}
