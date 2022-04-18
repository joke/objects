package io.github.joke.objects.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.inject.Inject;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

// Generate derived class
public class ClassGenerator {

    private final TypeElement element;

    @Inject
    public ClassGenerator(final TypeElement element) {
        this.element = element;
    }

    public TypeSpec.Builder getTypeBuilder() {
        final ClassName className = ClassName.get(element);
        final ClassName implementationName = className.peerClass(className.simpleName() + "Impl");

        final TypeSpec.Builder builder = classBuilder(implementationName)
                .addModifiers(PUBLIC);

        if (element.getKind().isInterface()) {
            return builder.addSuperinterface(element.asType());
        }
        return builder.superclass(element.asType());
    }
}
