package io.github.joke.objects.customize;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.github.joke.objects.customize.Annotate.Target.AUTO;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Documented
@Retention(SOURCE)
@Target({TYPE, METHOD})
@Repeatable(Annotate.Annotates.class)
public @interface Annotate {

    Class<? extends Annotation> value();

    String argument() default "";

    Target[] targets() default {AUTO};

    enum Target {
        AUTO,
        CONSTRUCTOR,
        FIELD,
        GETTER,
        SETTER
    }

    @Documented
    @Retention(SOURCE)
    @java.lang.annotation.Target({TYPE, METHOD})
    @interface Annotates {
        Annotate[] value() default {};
    }
}
