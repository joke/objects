package io.github.joke.objects.handlers;

import com.squareup.javapoet.JavaFile;
import dagger.BindsInstance;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;
import java.util.Set;

@NotNull
public interface Handler {

    Set<JavaFile> process();

    interface Builder<T extends Handler, B extends Builder<?, ?>> {

        @BindsInstance
        B typeElement(TypeElement typeElement);

        T build();
    }
}
