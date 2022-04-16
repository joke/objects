package io.github.joke.objects;

import com.google.auto.common.BasicAnnotationProcessor.Step;
import com.google.common.collect.ImmutableSetMultimap;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Map.Entry;
import java.util.Set;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static java.util.Collections.singleton;
import static java.util.function.Function.identity;
import static org.apache.commons.lang3.reflect.TypeUtils.getTypeArguments;

public abstract class AbstractStep<T extends Annotation, E extends Element> implements Step {

    protected final Filer filer;

    private final String annotation;

    protected AbstractStep(final Filer filer) {
        this.filer = filer;
        this.annotation = getTypeArguments(this.getClass(), AbstractStep.class).values().stream()
                .filter(Class.class::isInstance)
                .map(Class.class::cast)
                .findFirst()
                .map(Class::getTypeName)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    final public Set<String> annotations() {
        return singleton(annotation);
    }

    protected abstract boolean processInternal(final E element);

    @Override
    final public Set<? extends Element> process(final ImmutableSetMultimap<String, Element> elementsByAnnotation) {
        return elementsByAnnotation.get(annotation)
                .stream()
                .collect(toMap(identity(), x -> processInternal((E)x)))
                .entrySet().stream()
                .filter(entry -> !entry.getValue() )
                .map(Entry::getKey)
                .collect(toSet());
    }
}
