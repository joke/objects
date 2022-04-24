package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.generator.GeneratorUtils.determineSetterName;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

@NotNull
public class SettersGenerator {

    private final List<FieldSpec> properties;

    @Inject
    public SettersGenerator(@Named("properties") final List<FieldSpec> properties) {
        this.properties = properties;
    }

    public Set<MethodSpec> getSetters() {
        return properties.stream()
                .map(this::buildSetter)
                .collect(toSet());
    }

    private MethodSpec buildSetter(final FieldSpec fieldSpec) {
        final String setterName = determineSetterName(fieldSpec.name);
        return MethodSpec.methodBuilder(setterName)
                .addModifiers(PUBLIC)
                .addParameter(fieldSpec.type, fieldSpec.name, FINAL)
                .addStatement("this.$N = $N", fieldSpec.name, fieldSpec.name)
                .build();
    }

}
