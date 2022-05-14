package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.joke.objects.scopes.TypeElementScope;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Named;
import javax.lang.model.element.TypeElement;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static javax.lang.model.element.Modifier.ABSTRACT;
import static javax.lang.model.element.Modifier.PUBLIC;

@NotNull
@TypeElementScope
public class AccessorInterfaceGenerator {

    private final TypeElement typeElement;
    private final AnnotationSpec generatedAnnotation;

    @Inject
    public AccessorInterfaceGenerator(final TypeElement typeElement, @Named("generated") final AnnotationSpec generatedAnnotation) {
        this.typeElement = typeElement;
        this.generatedAnnotation = generatedAnnotation;
    }

    public Set<JavaFile> getJavaFile() {
        final Optional<ClassName> accessorInterface = typeElement.getInterfaces().stream()
                .filter(interface_ -> interface_.toString().equals(typeElement.getSimpleName() + "Accessors"))
                .findFirst()
                .map(ClassName::get)
                .filter(ClassName.class::isInstance)
                .map(ClassName.class::cast);

        if (!accessorInterface.isPresent()) {
            return emptySet();
        }

        final TypeSpec typeSpec = TypeSpec.interfaceBuilder(accessorInterface.get())
                .addAnnotation(generatedAnnotation)
                .addMethod(MethodSpec.methodBuilder("getZoo")
                        .returns(String.class)
                        .addModifiers(ABSTRACT, PUBLIC)
                        .build())
                .build();

        final JavaFile javaFile = JavaFile.builder(ClassName.get(typeElement).packageName(), typeSpec).build();
        return singleton(javaFile);

    }
}
