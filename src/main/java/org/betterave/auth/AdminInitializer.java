package org.betterave.auth;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Requires;
import org.apache.felix.ipojo.annotations.Validate;
import org.betterave.enums.Role;
import org.betterave.model.User;
import org.jasypt.util.password.PasswordEncryptor;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.jcrom.util.PathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wisdom.api.annotations.Model;
import org.wisdom.api.configuration.ApplicationConfiguration;
import org.wisdom.jcrom.object.JcrCrud;

import javax.jcr.query.Query;

/**
 * Initiliazer used to create the first plateform administrator from the application configuration.
 * <p/>
 * Created by antoine on 14/06/15.
 */
@Component
@Instantiate
public class AdminInitializer {

    public final static Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    @Model(User.class)
    JcrCrud<User, String> userCrud;

    @Requires
    ApplicationConfiguration applicationConfiguration;

    private final PasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    @Validate
    public void checkAdminConfiguration() {
        try {
            String email = applicationConfiguration.getOrDie("betterave.admin.email");
            String password = applicationConfiguration.getOrDie("betterave.admin.password");
            User user = userCrud.findOneByQuery(User.findByEmail(email), Query.JCR_SQL2);
            if (user == null) {
                createFirstAdmin(email, password);
            }
        } catch (RuntimeException e) {
            logger.error("You need to define an admin user in application configuration");
            logger.error("Please define 'betterave.admin.email' and 'betterave.admin.password'");
            throw e;
        }
    }

    private void createFirstAdmin(String email, String password) {
        User user = new User();
        user.setPath(User.ROOT_PATH);
        user.setEmail(email);
        user.setUuid(PathUtils.createValidName(email));
        user.setRole(Role.ADMIN);
        String encryptedPassword = passwordEncryptor.encryptPassword(password);
        user.setPassword(encryptedPassword);
        userCrud.save(user);
        logger.info("First admin user created from application configuration : " + email);
    }

}
