package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.generator.GeneratorUtils.determinePropertyName;
import static io.github.joke.objects.generator.GeneratorUtils.filterGetters;
import static javax.lang.model.util.ElementFilter.methodsIn;

public class GettersGenerator implements Generator<Set<MethodSpec>> {

    @Override
    public Set<MethodSpec> generate(final TypeElement element) {
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
