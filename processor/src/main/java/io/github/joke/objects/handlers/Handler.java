package io.github.joke.objects.handlers;

import com.squareup.javapoet.JavaFile;
import dagger.BindsInstance;
import io.github.joke.objects.processor.Processor;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.TypeElement;
import java.util.Set;

@NotNull
public interface Handler {

    Set<JavaFile> process();

    interface Factory<T extends Handler> {

        T create(Processor processor, @BindsInstance TypeElement typeElement);
    }
}
