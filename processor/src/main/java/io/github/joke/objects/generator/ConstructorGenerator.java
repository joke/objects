package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

@NotNull
public class ConstructorGenerator {

    private final List<FieldSpec> properties;

    @Inject
    public ConstructorGenerator(@Named("properties") final List<FieldSpec> properties) {
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
            builder.addParameter(field.type, field.name, FINAL);
            builder.addStatement("this.$N = $N", field.name, field.name);
        });
        return builder.build();
    }

}
