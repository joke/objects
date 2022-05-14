package io.github.joke.objects.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import io.github.joke.objects.AnnotationProcessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import static javax.lang.model.SourceVersion.RELEASE_8;
import static org.apache.commons.lang3.ObjectUtils.compare;

@NotNull
@Singleton
@Named("generated")
public class GeneratedAnnotationGenerator {

    private final ProcessingEnvironment processingEnvironment;

    @Inject
    public GeneratedAnnotationGenerator(final ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public AnnotationSpec getGeneratedAnnotation() {
        final boolean isJava9 = compare(processingEnvironment.getSourceVersion(), RELEASE_8) > 0;
        final String generatorAnnotationPackage = isJava9 ? "javax.annotation.processing" : "javax.annotation";
        return AnnotationSpec.builder(ClassName.get(generatorAnnotationPackage, "Generated"))
                .addMember("value", "$S", AnnotationProcessor.class.getCanonicalName())
                .build();
    }
}
