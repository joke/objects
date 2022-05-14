package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.scopes.TypeElementScope;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

@NotNull
@TypeElementScope
public class ConstructorGenerator {

    private final List<Attribute> attributes;

    @Inject
    public ConstructorGenerator(final List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Set<MethodSpec> getConstructors() {
        return singleton(buildAllArgsConstructor());
    }

    private MethodSpec buildAllArgsConstructor() {
        final MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC);
        attributes.forEach(attribute -> {
            final String name = attribute.getName();

            final Set<AnnotationSpec> annotations = attribute.getAnnotations().stream()
                    .map(AnnotationSpec::get)
                    .collect(toSet());

            final ParameterSpec parameter = ParameterSpec.builder(TypeName.get(attribute.getType()), name, FINAL)
                    .addAnnotations(annotations)
                    .build();

            builder.addParameter(parameter);
            if (attribute.isNotNull()) {
                builder.addStatement("this.$N = $T.requireNonNull($N)", name, Objects.class, name);
            } else {
                builder.addStatement("this.$N = $N", name, name);
            }
        });

        return builder.build();
    }

}
