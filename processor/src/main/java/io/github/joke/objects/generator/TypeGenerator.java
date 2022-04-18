package io.github.joke.objects.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.inject.Inject;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class TypeGenerator {

    private final Set<MethodSpec> methods;
    private final Set<FieldSpec> fields;
    private final TypeElement typeElement;
    private final TypeSpec.Builder type;

    @Inject
    public TypeGenerator(final TypeElement typeElement, final TypeSpec.Builder type, final Set<MethodSpec> methods, final Set<FieldSpec> fields) {
        this.type = type;
        this.methods = methods;
        this.fields = fields;
        this.typeElement = typeElement;
    }

    public JavaFile getJavaFile() {
        final TypeSpec spec = type
                .addFields(fields)
                .addMethods(methods)
                .build();

        return JavaFile.builder(ClassName.get(typeElement).packageName(), spec)
                .build();
    }

}
