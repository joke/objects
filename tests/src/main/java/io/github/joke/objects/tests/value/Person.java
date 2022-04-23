package io.github.joke.objects.tests.value;

import io.github.joke.objects.Value;

import java.util.List;

@Value
public interface Person {

    String getName();

    List<Person> getParents();

}
