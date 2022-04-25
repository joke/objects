package io.github.joke.objects.tests.immutable;

import io.github.joke.objects.Immutable;

import java.util.List;

@Immutable
public interface Person {

    String getName();

    List<Person> getParents();

}
