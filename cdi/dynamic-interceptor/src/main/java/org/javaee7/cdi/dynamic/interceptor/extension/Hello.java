package org.javaee7.cdi.dynamic.interceptor.extension;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.interceptor.InterceptorBinding;


@Inherited
@InterceptorBinding
@Retention(RUNTIME)
@Target(METHOD)
public @interface Hello {
}