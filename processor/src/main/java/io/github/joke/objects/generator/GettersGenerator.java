package io.github.joke.objects.generator;

import com.squareup.javapoet.MethodSpec;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static com.google.auto.common.MoreTypes.isTypeOf;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.util.ElementFilter.methodsIn;
import static org.apache.commons.lang3.StringUtils.startsWithAny;

public class GettersGenerator implements Generator<Set<MethodSpec>> {

    @Override
    public Set<MethodSpec> generate(final TypeElement element) {
        final List<ExecutableElement> methods = methodsIn(element.getEnclosedElements());

        final List<ExecutableElement> getter = methods.stream()
                .filter(method -> !method.getModifiers().contains(STATIC))
                .filter(method -> !isTypeOf(Void.TYPE, method.getReturnType()))
                .filter(method -> !method.isVarArgs())
                .filter(method -> method.getParameters().isEmpty())
                .filter(method -> startsWithAny(method.getSimpleName(), "get", "is"))
                .collect(toList());

        return getter.stream()
                .map(this::buildGetter)
                .collect(toSet());
    }

    private MethodSpec buildGetter(final ExecutableElement element) {
        return MethodSpec.overriding(element)
                .addStatement("return $L", "null")
                .build();
    }

}
