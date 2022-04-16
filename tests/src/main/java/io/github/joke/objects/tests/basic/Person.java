package io.github.joke.objects.tests.basic;

import io.github.joke.objects.Bean;

import java.util.List;

@Bean
public interface Person {

    String getName();

    List<Person> getParents();

}
