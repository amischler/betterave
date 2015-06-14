package org.betterave.auth;

import org.betterave.enums.Role;

import java.lang.annotation.*;

/**
 * This annotation is used to specify the user Roles allowed to access to a given route when using the
 * RoleAuthenticator.
 * <p/>
 * Created by antoine on 14/06/15.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Roles {

    Role[] value();

}
