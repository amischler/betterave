package org.betterave.admin;

import org.betterave.auth.RoleAuthenticator;
import org.betterave.auth.Roles;
import org.betterave.enums.Role;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Path;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;

/**
 * Created by antoine on 14/06/15.
 */
@Path("/admin")
@Controller
public class AdminController extends DefaultController {

    @View("admin/home")
    Template homeTemplate;

    @Authenticated(RoleAuthenticator.NAME)
    @Roles(Role.ADMIN)
    @Route(method = HttpMethod.GET, uri = "")
    public Result index() {
        return ok(render(homeTemplate));
    }

}
