package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;

import javax.inject.Inject;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.generator.GeneratorUtils.determinePropertyName;
import static io.github.joke.objects.generator.GeneratorUtils.filterGetters;
import static javax.lang.model.util.ElementFilter.methodsIn;

public class GettersGenerator {

    private final TypeElement element;

    @Inject
    public GettersGenerator(final TypeElement element) {
        this.element = element;
    }

    public Set<MethodSpec> getGetters() {
        final List<ExecutableElement> methods = methodsIn(element.getEnclosedElements());

        return filterGetters(methods).stream()
                .map(this::buildGetter)
                .collect(toSet());
    }

    private MethodSpec buildGetter(final ExecutableElement element) {
        final String propertyName = determinePropertyName(element);
        return MethodSpec.overriding(element)
                .addStatement("return $L", propertyName)
                .build();
    }

}
