package com.intuso.housemate.globalserver.servers.ioc;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by tomc on 24/01/17.
 */
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface ServerAddress {
}
