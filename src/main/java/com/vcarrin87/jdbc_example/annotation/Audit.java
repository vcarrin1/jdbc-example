package com.vcarrin87.jdbc_example.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Audit {

    String action();

    String resource() default "";

    String resourceId() default "";

    String details() default "";
}
