package org.betterave.auth;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.betterave.model.User;
import org.wisdom.api.annotations.Model;
import org.wisdom.api.http.Context;
import org.wisdom.api.http.Result;
import org.wisdom.api.http.Results;
import org.wisdom.api.security.Authenticator;
import org.wisdom.jcrom.object.JcrCrud;

import javax.jcr.query.Query;
import java.util.Arrays;

/**
 * Created by antoine on 14/06/15.
 */
@Component
@Provides
@Instantiate
public class RoleAuthenticator implements Authenticator {

    public static final String NAME = "role-authenticator";

    @Model(User.class)
    JcrCrud<User, String> userCrud;

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getUserName(Context context) {
        Roles roles = context.route().getControllerMethod().getAnnotation(Roles.class);
        if (roles == null) {
            throw new AuthenticationException("You need to define a @Roles annotation to specify the allowed roles on your method " + context.route().getControllerMethod());
        }
        String userEmail = context.session().get(AuthController.SESSION_EMAIL);
        if (userEmail != null) {
            User user = userCrud.findOneByQuery(User.findByEmail(userEmail), Query.JCR_SQL2);
            if (user != null && Arrays.asList(roles.value()).contains(user.getRole())) {
                return userEmail;
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Context context) {
        return Results.redirect("/login");
    }

}
