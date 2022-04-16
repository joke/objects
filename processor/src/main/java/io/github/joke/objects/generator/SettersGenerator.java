package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static io.github.joke.objects.generator.GeneratorUtils.determinePropertyName;
import static io.github.joke.objects.generator.GeneratorUtils.determineSetterName;
import static io.github.joke.objects.generator.GeneratorUtils.filterGetters;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.util.ElementFilter.methodsIn;

public class SettersGenerator implements Generator<Set<MethodSpec>> {

    @Override
    public Set<MethodSpec> generate(final TypeElement element) {
        final List<ExecutableElement> methods = methodsIn(element.getEnclosedElements());

        return filterGetters(methods).stream()
                .map(this::buildSetter)
                .collect(toSet());
    }

    private MethodSpec buildSetter(final ExecutableElement element) {
        final String setterName = determineSetterName(element);
        final String propertyName = determinePropertyName(element);
        final TypeName type = TypeName.get(element.getReturnType());

        return MethodSpec.methodBuilder(setterName)
                .addModifiers(PUBLIC)
                .addParameter(type, propertyName, FINAL)
                .addStatement("this.$N = $N", propertyName, propertyName)
                .build();
    }

}
