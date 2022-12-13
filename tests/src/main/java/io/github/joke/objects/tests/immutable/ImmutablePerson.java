package io.github.joke.objects.tests.immutable;

import io.github.joke.objects.Immutable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Immutable
public abstract class ImmutablePerson {

    @Nullable
    abstract String getNullableName();

    @NotNull
    abstract String getNotNullName();
}
