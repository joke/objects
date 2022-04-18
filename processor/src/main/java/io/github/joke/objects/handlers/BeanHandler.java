package io.github.joke.objects.handlers;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import dagger.Component;
import dagger.Provides;
import dagger.multibindings.ElementsIntoSet;
import io.github.joke.objects.generator.ClassGenerator;
import io.github.joke.objects.generator.GettersGenerator;
import io.github.joke.objects.generator.PropertiesGenerator;
import io.github.joke.objects.generator.SettersGenerator;
import io.github.joke.objects.generator.TypeGenerator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Inject;
import java.util.Set;

import static io.github.joke.objects.handlers.DaggerBeanHandler_Factory.builder;

public class BeanHandler extends AbstractHandler {

    @Inject
    public BeanHandler(final ProcessingEnvironment processingEnvironment) {
        super(builder(), processingEnvironment);
    }

    @dagger.Module
    public interface Module {
        // all the generators

        @Provides
        @ElementsIntoSet
        static Set<FieldSpec> provideProperties(final PropertiesGenerator propertiesGenerator) {
            return propertiesGenerator.getProperties();
        }

        @Provides
        @ElementsIntoSet
        static Set<MethodSpec> provideGetters(final GettersGenerator gettersGenerator) {
            return gettersGenerator.getGetters();
        }

        @Provides
        @ElementsIntoSet
        static Set<MethodSpec> provideSetters(final SettersGenerator settersGenerator) {
            return settersGenerator.getSetters();
        }

        @Provides
        static TypeSpec.Builder provideTypeSpecBuilder(final ClassGenerator classGenerator) {
            return classGenerator.getTypeBuilder();
        }

        @Provides
        static JavaFile provideJavaFile(final TypeGenerator typeGenerator) {
            return typeGenerator.getJavaFile();
        }

        @Provides
        static Filer provideFiler(final ProcessingEnvironment processingEnvironment) {
            return processingEnvironment.getFiler();
        }
    }

    @Component(modules = Module.class)
    interface Factory extends AbstractHandler.Factory {
        @Component.Builder
        interface Builder extends AbstractHandler.Builder<Factory> {
        }
    }

}
