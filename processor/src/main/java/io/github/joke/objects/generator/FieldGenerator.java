package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.scopes.AttributeScope;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.PRIVATE;

@NotNull
@AttributeScope
public class FieldGenerator {

    private final Attribute attribute;

    @Inject
    public FieldGenerator(final Attribute attribute) {
        this.attribute = attribute;
    }

    public Set<FieldSpec> generate() {
        return singleton(buildField(attribute));
    }

    private FieldSpec buildField(final Attribute attribute) {
        final Set<AnnotationSpec> annotations = attribute.getAnnotations().stream()
                .map(AnnotationSpec::get)
                .collect(toSet());
        return FieldSpec.builder(TypeName.get(attribute.getType()), attribute.getName(), PRIVATE)
                .addAnnotations(annotations)
                .build();
    }

}
