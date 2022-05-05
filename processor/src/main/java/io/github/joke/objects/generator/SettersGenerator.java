package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;
import io.github.joke.objects.handlers.ElementScope;
import io.github.joke.objects.generator.scanner.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

@NotNull
@ElementScope
public class SettersGenerator {

    private final List<Property> properties;

    @Inject
    public SettersGenerator(final List<Property> properties) {
        this.properties = properties;
    }

    public Set<MethodSpec> getSetters() {
        return properties.stream()
                .map(this::buildSetter)
                .collect(toSet());
    }

    private MethodSpec buildSetter(final Property property) {
        final String propertyName = property.getName();
        final String setterName = determineSetterName(propertyName);
        return MethodSpec.methodBuilder(setterName)
                .addModifiers(PUBLIC)
                .addParameter(property.getType(), propertyName, FINAL)
                .addStatement("this.$N = $N", propertyName, propertyName)
                .build();
    }

    public static String determineSetterName(final String propertyName) {
        return "set" + capitalize(propertyName);
    }

}
