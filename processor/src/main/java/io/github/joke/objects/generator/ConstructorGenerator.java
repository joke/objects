package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;
import io.github.joke.objects.handlers.TypeElementScope;
import io.github.joke.objects.generator.scanner.Property;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

@NotNull
@TypeElementScope
public class ConstructorGenerator {

    private final List<Property> properties;

    @Inject
    public ConstructorGenerator(final List<Property> properties) {
        this.properties = properties;
    }

    public Set<MethodSpec> getConstructors() {
        final MethodSpec allArgsConstructor = buildAllArgsConstructor();
        return singleton(allArgsConstructor);
    }

    private MethodSpec buildAllArgsConstructor() {
        final MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC);
        properties.forEach(field -> {
            final String name = field.getName();
            builder.addParameter(field.getType(), name, FINAL);
            builder.addStatement("this.$N = $N", name, name);
        });
        return builder.build();
    }

}
