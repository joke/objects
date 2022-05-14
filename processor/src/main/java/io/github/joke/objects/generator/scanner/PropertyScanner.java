package io.github.joke.objects.generator.scanner;

import com.squareup.javapoet.TypeName;
import io.github.joke.objects.handlers.TypeElementScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import javax.annotation.processing.Messager;
import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.type.TypeKind.VOID;
import static javax.tools.Diagnostic.Kind.ERROR;
import static org.apache.commons.lang3.RegExUtils.removeFirst;
import static org.apache.commons.lang3.StringUtils.startsWithAny;
import static org.apache.commons.lang3.StringUtils.uncapitalize;

@TypeElementScope
public class PropertyScanner {

    private final Elements elements;
    private final Messager messager;
    private final TypeElement element;

    @Inject
    public PropertyScanner(final Elements elements, final Messager messager, final TypeElement element) {
        this.elements = elements;
        this.messager = messager;
        this.element = element;
    }

    public List<Property> getProperties() {
        final List<Property> properties = determineProperties();
        checkForDuplicates(properties);
        return properties;
    }

    @NotNull
    @VisibleForTesting
    protected List<Property> determineProperties() {
        return elements.getAllMembers(element)
                .stream()
                .map(this::buildPropertyInfo)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @VisibleForTesting
    protected void checkForDuplicates(final List<Property> properties) {
        final Set<String> names = new HashSet<>();
        properties.stream()
                .filter(property -> !names.add(property.getName()))
                .map(Property::getOrigin)
                .forEach(this::reportDuplicate);
    }

    private void reportDuplicate(final Element element) {
        messager.printMessage(ERROR, "Trying to generate a property with the same name. The property might already exist with a base class or it would be generated from an property method.", element);
    }

    @Nullable
    @VisibleForTesting
    protected Property buildPropertyInfo(final Element element) {
        switch (element.getKind()) {
            case FIELD:
                return fromField((VariableElement) element);
            case METHOD:
                return fromMethod((ExecutableElement) element);
            default:
                return null;
        }
    }

    @Nullable
    @VisibleForTesting
    protected Property fromField(final VariableElement element) {
        // private fields of abstract class cannot be accessed anyway
        if (element.getModifiers().contains(PRIVATE)) {
            return null;
        }
        return new Property(element, element.getSimpleName().toString(), false, TypeName.get(element.asType()));
    }

    @Nullable
    @VisibleForTesting
    protected Property fromMethod(final ExecutableElement element) {
        if (isExcludedFromPropertyGeneration(element)) {
            return null;
        }

        final String propertyName = determinePropertyName(element);
        return new Property(element, propertyName, true, TypeName.get(element.getReturnType()));
    }

    @VisibleForTesting
    protected boolean isExcludedFromPropertyGeneration(final ExecutableElement element) {
        return !element.getModifiers().contains(ABSTRACT) ||
                element.getModifiers().contains(STATIC) ||
                element.getReturnType().getKind().equals(VOID) ||
                element.isVarArgs() ||
                !element.getParameters().isEmpty();
    }

    @VisibleForTesting
    protected String determinePropertyName(final ExecutableElement element) {
        final String name = element.getSimpleName().toString();
        if (startsWithAny(name, "get", "is")) {
            return uncapitalize(removeFirst(name, "^(get|is)"));
        }
        return name;
    }

}
