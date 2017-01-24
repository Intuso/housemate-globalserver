package com.intuso.housemate.globalserver;

/**
 * Created by tomc on 24/01/17.
 */
public class GlobalServerException extends RuntimeException {
    public GlobalServerException() {}

    public GlobalServerException(String s) {
        super(s);
    }

    public GlobalServerException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public GlobalServerException(Throwable throwable) {
        super(throwable);
    }

    public GlobalServerException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
