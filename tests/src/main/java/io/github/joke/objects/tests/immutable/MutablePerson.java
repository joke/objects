package io.github.joke.objects.tests.immutable;

import io.github.joke.objects.Mutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mutable
public abstract class MutablePerson {

    @Nullable
    abstract String getNullableName();

    @NotNull
    abstract String getNonNullName();
}
