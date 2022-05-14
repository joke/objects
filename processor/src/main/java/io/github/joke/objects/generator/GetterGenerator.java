package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.MethodSpec;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.scopes.AttributeScope;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static com.squareup.javapoet.MethodSpec.overriding;
import static java.util.Collections.singleton;

@NotNull
@AttributeScope
public class GetterGenerator {

    private final Attribute attribute;

    @Inject
    public GetterGenerator(final Attribute attribute) {
        this.attribute = attribute;
    }

    public Set<MethodSpec> generate() {
        final Set<AnnotationSpec> annotations = attribute.getAnnotations().stream()
                .map(AnnotationSpec::get)
                .collect(toSet());

        final MethodSpec methodSpec = overriding(attribute.getOrigin())
                .addAnnotations(annotations)
                .addStatement("return $L", attribute.getName())
                .build();
        return singleton(methodSpec);
    }
}
