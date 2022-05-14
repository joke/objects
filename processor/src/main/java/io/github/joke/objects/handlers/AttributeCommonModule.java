package io.github.joke.objects.handlers;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.FieldGenerator;
import io.github.joke.objects.generator.GetterGenerator;
import io.github.joke.objects.scopes.AttributeScope;

import java.util.Set;

@dagger.Module
public interface AttributeCommonModule {
    @Provides
    @AttributeScope
    @ElementsIntoSet
    static Set<FieldSpec> getField(final FieldGenerator fieldGenerator) {
        return fieldGenerator.getField();
    }

    @Provides
    @AttributeScope
    @ElementsIntoSet
    static Set<MethodSpec> provideGetter(final GetterGenerator generator) {
        return generator.generate();
    }
}
