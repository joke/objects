package io.github.joke.objects.tests.mutable;

import io.github.joke.objects.Mutable;

import java.util.List;

@Mutable
public interface Person {

    String getName();

    List<Person> getParents();

}
