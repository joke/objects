package io.github.joke.objects.tests.immutable;

import io.github.joke.objects.Mutable;

@Mutable
public abstract class Address {

    private String street;

    String city;

    protected String zipCode;

    public String country;

    public abstract String getZoo();
    protected abstract String getZoo2();
    abstract String getZoo3();

//    abstract String getCity();


}
