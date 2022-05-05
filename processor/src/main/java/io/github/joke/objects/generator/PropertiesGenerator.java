package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import io.github.joke.objects.handlers.ElementScope;
import io.github.joke.objects.generator.scanner.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.lang.model.element.Modifier.PRIVATE;

@NotNull
@ElementScope
public class PropertiesGenerator {

    private final List<Property> properties;

    @Inject
    public PropertiesGenerator(final List<Property> properties) {
        this.properties = properties;
    }

    public Set<FieldSpec> getProperties() {
        return properties.stream()
                .map(this::buildProperties)
                .collect(toSet());
    }

    private FieldSpec buildProperties(final Property property) {
        return FieldSpec.builder(property.getType(), property.getName(), PRIVATE)
                .build();
    }

}
