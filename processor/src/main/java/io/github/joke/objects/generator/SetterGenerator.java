package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.scopes.AttributeScope;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.StringUtils.capitalize;

@NotNull
@AttributeScope
public class SetterGenerator {

    private final Attribute attributes;

    @Inject
    public SetterGenerator(final Attribute attribute) {
        this.attributes = attribute;
    }

    public Set<MethodSpec> generate() {
        return singleton(buildSetter(attributes));
    }

    protected MethodSpec buildSetter(final Attribute attribute) {
        final Set<AnnotationSpec> annotations = attribute.getAnnotations().stream()
                        .map(AnnotationSpec::get)
                        .collect(toSet());

        final String propertyName = attribute.getName();
        final String setterName = determineSetterName(propertyName);

        final ParameterSpec parameter = ParameterSpec.builder(TypeName.get(attribute.getType()), propertyName, FINAL)
                .addAnnotations(annotations)
                .build();

        return MethodSpec.methodBuilder(setterName)
                .addModifiers(PUBLIC)
                .addParameter(parameter)
                .addStatement("this.$N = $N", propertyName, propertyName)
                .build();
    }

    protected String determineSetterName(final String propertyName) {
        return "set" + capitalize(propertyName);
    }
}
