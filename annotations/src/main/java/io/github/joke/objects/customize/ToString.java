package io.github.joke.objects.customize;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static io.github.joke.objects.customize.ToString.Style.STRING_JOINER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Documented
@Retention(SOURCE)
@Target({TYPE})
public @interface ToString {

    Style style() default STRING_JOINER;

    enum Style {
        STRING_JOINER,
        TO_STRING_BUILDER
    }
}
