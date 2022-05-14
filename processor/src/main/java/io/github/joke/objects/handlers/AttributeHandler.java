package io.github.joke.objects.handlers;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import dagger.BindsInstance;
import io.github.joke.objects.generator.extractors.Attribute;
import io.github.joke.objects.processor.Processor;

import java.util.Set;

interface AttributeHandler {

    Set<FieldSpec> getFields();
    Set<MethodSpec> getMethods();

    interface Factory<T extends AttributeHandler> {
        T create(Processor processor, Handler handler, @BindsInstance Attribute attribute);
    }
}
