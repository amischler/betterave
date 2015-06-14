package org.betterave.auth;

/**
 * Created by antoine on 14/06/15.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }
}
