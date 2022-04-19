package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.SourceVersion.RELEASE_8;
import static javax.lang.model.element.Modifier.PUBLIC;
import static org.apache.commons.lang3.ObjectUtils.compare;

// Generate derived class
@NotNull
public class ClassGenerator {

    private final TypeElement element;
    private final ProcessingEnvironment processingEnvironment;

    @Inject
    public ClassGenerator(final TypeElement element, final ProcessingEnvironment processingEnvironment) {
        this.element = element;
        this.processingEnvironment = processingEnvironment;
    }

    public TypeSpec.Builder getTypeBuilder() {
        final ClassName className = ClassName.get(element);
        final ClassName implementationName = className.peerClass(className.simpleName() + "Impl");

        final TypeSpec.Builder builder = classBuilder(implementationName)
                .addAnnotation(getGeneratedAnnotation())
                .addModifiers(PUBLIC);

        if (element.getKind().isInterface()) {
            return builder.addSuperinterface(element.asType());
        }
        return builder.superclass(element.asType());
    }

    private AnnotationSpec getGeneratedAnnotation() {
        final boolean isJava9 = compare(processingEnvironment.getSourceVersion(), RELEASE_8) > 0;
        final String generatorAnnotationPackage = isJava9 ? "javax.annotation.processing" : "javax.annotation";
        return AnnotationSpec.builder(ClassName.get(generatorAnnotationPackage, "Generated"))
                .addMember("value", "$S", PropertiesGenerator.class.getCanonicalName())
                .build();
    }
}
