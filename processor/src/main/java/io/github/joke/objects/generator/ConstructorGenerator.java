package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

import javax.inject.Inject;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class ConstructorGenerator {

    private final TypeElement element;
    private final Set<FieldSpec> fields;

    @Inject
    public ConstructorGenerator(final TypeElement element, final Set<FieldSpec> fields) {
        this.element = element;
        this.fields = fields;
    }

    public Set<MethodSpec> getConstructors() {
        final MethodSpec allArgsConstructor = buildAllArgsConstructor();
        return singleton(allArgsConstructor);
    }

    private MethodSpec buildAllArgsConstructor() {
        final MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(PUBLIC);
        fields.forEach(field -> {
            builder.addParameter(field.type, field.name, FINAL);
            builder.addStatement("this.$N = $N", field.name, field.name);
        });
        return builder.build();
    }

}
