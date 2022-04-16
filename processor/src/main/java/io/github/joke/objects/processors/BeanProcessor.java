package io.github.joke.objects.processors;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.joke.objects.generator.ClassGenerator;
import io.github.joke.objects.generator.GettersGenerator;
import io.github.joke.objects.generator.PropertiesGenerator;
import io.github.joke.objects.generator.SettersGenerator;

import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class BeanProcessor extends DeferrableProcessor {

    public BeanProcessor(final Filer filer) {
        super(filer);
    }

    @Override
    protected JavaFile output(final TypeElement element) {
        final TypeSpec.Builder clazz = new ClassGenerator().generate(element);
        final Set<MethodSpec> getters = new GettersGenerator().generate(element);
        final Set<MethodSpec> setters = new SettersGenerator().generate(element);
        final Set<FieldSpec> properties = new PropertiesGenerator().generate(element);

        final TypeSpec spec = clazz
                .addFields(properties)
                .addMethods(getters)
                .addMethods(setters)
                .build();

        return JavaFile.builder(ClassName.get(element).packageName(), spec)
                .build();
    }

}
