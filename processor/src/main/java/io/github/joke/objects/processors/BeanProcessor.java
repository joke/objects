package io.github.joke.objects.processors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;

import static com.squareup.javapoet.TypeSpec.classBuilder;
import static javax.lang.model.element.Modifier.PUBLIC;

public class BeanProcessor extends DeferrableProcessor {

    public BeanProcessor(final Filer filer) {
        super(filer);
    }

    @Override
    protected JavaFile output(final TypeElement element) {
        final ClassName className = ClassName.get(element);
        final ClassName implementationName = className.peerClass(className.simpleName() + "Impl");

        final TypeSpec spec = classBuilder(implementationName)
                .addSuperinterface(element.asType())
                .addModifiers(PUBLIC)
                .build();

        return JavaFile.builder(implementationName.packageName(), spec)
                .build();
    }

}
