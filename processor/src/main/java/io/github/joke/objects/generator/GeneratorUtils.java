package io.github.joke.objects.generator;

import javax.lang.model.element.ExecutableElement;
import java.util.Collection;
import java.util.Set;

import static java.util.stream.Collectors.toSet;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.type.TypeKind.VOID;
import static org.apache.commons.lang3.RegExUtils.removeFirst;
import static org.apache.commons.lang3.RegExUtils.replacePattern;
import static org.apache.commons.lang3.StringUtils.startsWithAny;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

public final class GeneratorUtils {

    public static String determinePropertyName(final ExecutableElement element) {
        final String strippedName = removeFirst(element.getSimpleName().toString(), "^(get|is)");
        return uncapitalize(strippedName);
    }

    public static String determineSetterName(final ExecutableElement element) {
        return replacePattern(element.getSimpleName().toString(), "^(get|is)", "set");
    }

    public static Set<ExecutableElement> filterGetters(final Collection<ExecutableElement> elements) {
        return elements.stream()
                .filter(method -> !method.getModifiers().contains(STATIC))
                .filter(method -> !method.getReturnType().getKind().equals(VOID))
                .filter(method -> !method.isVarArgs())
                .filter(method -> method.getParameters().isEmpty())
                .filter(method -> startsWithAny(method.getSimpleName(), "get", "is"))
                .collect(toSet());
    }

}
