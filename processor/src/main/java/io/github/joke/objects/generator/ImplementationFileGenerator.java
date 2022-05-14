package io.github.joke.objects.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.joke.objects.handlers.TypeElementScope;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static java.util.Collections.singleton;

@NotNull
@TypeElementScope
public class ImplementationFileGenerator {

    private final Set<MethodSpec> methods;
    private final Set<FieldSpec> fields;
    private final TypeElement typeElement;
    private final TypeSpec.Builder type;

    @Inject
    public ImplementationFileGenerator(final TypeElement typeElement, final TypeSpec.Builder type, final Set<MethodSpec> methods, final Set<FieldSpec> fields) {
        this.type = type;
        this.methods = methods;
        this.fields = fields;
        this.typeElement = typeElement;
    }

    public Set<JavaFile> getJavaFile() {
        final TypeSpec spec = type
                .addOriginatingElement(typeElement)
                .addFields(fields)
                .addMethods(methods)
                .build();
        final JavaFile javaFile = JavaFile.builder(ClassName.get(typeElement).packageName(), spec)
                .build();
        return singleton(javaFile);
    }

}
