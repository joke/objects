package io.github.joke.objects.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

// Generate derived class
public class ClassGenerator implements Generator<TypeSpec.Builder> {

    @Override
    public TypeSpec.Builder generate(final TypeElement element) {
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
