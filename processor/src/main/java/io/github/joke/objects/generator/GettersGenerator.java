package io.github.joke.objects.generator;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.generator.GeneratorUtils.determineGetterName;
import static javax.lang.model.element.Modifier.PUBLIC;

@NotNull
public class GettersGenerator {

    private final List<FieldSpec> properties;

    @Inject
    public GettersGenerator(@Named("properties") final List<FieldSpec> properties) {
        this.properties = properties;
    }

    public Set<MethodSpec> getGetters() {
        return properties.stream()
                .map(this::buildGetter)
                .collect(toSet());
    }

    private MethodSpec buildGetter(final FieldSpec property) {
        final String getterName = determineGetterName(property.name);
        return MethodSpec.methodBuilder(getterName)
                .returns(property.type)
                .addModifiers(PUBLIC)
                .addAnnotation(Override.class)
                .addStatement("return $L", property.name)
                .build();
    }

}
