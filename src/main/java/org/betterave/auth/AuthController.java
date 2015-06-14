package org.betterave.auth;

import org.betterave.model.User;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.http.Results;
import org.wisdom.api.templates.Template;
import org.wisdom.jcrom.object.JcrCrud;

import javax.jcr.query.Query;

/**
 * Created by antoine on 13/06/15.
 */
@Controller
public class AuthController extends DefaultController {

    public static final String SESSION_EMAIL = "betterave.user.email";

    @Model(User.class)
    JcrCrud<User, String> userCrud;

    @View("auth/login")
    Template loginTemplate;

    private final PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    /**
     * @return the login page. If the authentication is disabled, return the default monitor page.
     */
    @Route(method = HttpMethod.GET, uri = "/login")
    public Result login() {
        return ok(render(loginTemplate));
    }

    /**
     * @return the login page. The user is logged out.
     */
    @Route(method = HttpMethod.GET, uri = "/logout")
    public Result logout() {
        String email = session().get(SESSION_EMAIL);
        if (email != null) {
            context().session().remove(SESSION_EMAIL);
            logger().info(email + " logged out.");
        } else {
            logger().info("No user logged in.");
        }
        return login();
    }

    /**
     * Authenticates the user.
     *
     * @param email    the email
     * @param password the password
     * @return the default page if the authentication succeed, the login page otherwise.
     */
    @Route(method = HttpMethod.POST, uri = "/login")
    public Result authenticate(@FormParameter("email") String email, @FormParameter("password") String password) {
        User user = userCrud.findOneByQuery(User.findByEmail(email), Query.JCR_SQL2);
        if (user != null && passwordEncryptor.checkPassword(password, user.getPassword())) {
            session().put(SESSION_EMAIL, email);
            logger().info("Authentication successful - {}", email);
            return Results.redirect("/" + user.getRole().name().toLowerCase());
        } else {
            logger().info("Authentication failed - {}", email);
            context().flash().error("Authentication failed - check your credentials");
            return login();
        }
    }

}
