package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import io.github.joke.objects.handlers.TypeElementScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import javax.inject.Inject;
import javax.inject.Named;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

// Generate derived class
@NotNull
@TypeElementScope
public class ImplementationTypeGenerator {

    private final TypeElement element;
    private final AnnotationSpec generatedAnnotation;

    @Inject
    public ImplementationTypeGenerator(final TypeElement element, @Named("generated") final AnnotationSpec generatedAnnotation) {
        this.element = element;
        this.generatedAnnotation = generatedAnnotation;
    }

    public TypeSpec.Builder getTypeBuilder() {
        final ClassName implementationName = determineClassName();

        final TypeSpec.Builder builder = classBuilder(implementationName)
                .addAnnotation(generatedAnnotation)
                .addModifiers(PUBLIC);

        if (element.getKind().isInterface()) {
            return builder.addSuperinterface(element.asType());
        }
        return builder.superclass(element.asType());
    }

    @VisibleForTesting
    protected ClassName determineClassName() {
        final ClassName className = ClassName.get(element);
        return className.peerClass(className.simpleName() + "Impl");
    }

}
