package io.github.joke.objects.tests.immutable;

import io.github.joke.objects.Immutable;
import io.github.joke.objects.customize.ToString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ToString
@Immutable
public abstract class ImmutablePerson {

    @Nullable
    abstract String getNullableName();

    @NotNull
    abstract String getNotNullName();
}
