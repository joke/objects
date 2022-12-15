package io.github.joke.objects.tests.immutable;

import io.github.joke.objects.Mutable;
import io.github.joke.objects.customize.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mutable
@ToString
public abstract class MutablePerson {

    @Nullable
    abstract String getNullableName();

    @NotNull
    abstract String getNonNullName();
}
