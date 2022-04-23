package io.github.joke.objects.tests.bean;

import io.github.joke.objects.Bean;

import java.util.List;

@Bean
public interface Person {

    String getName();

    List<Person> getParents();

}
