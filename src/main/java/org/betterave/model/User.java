package org.betterave.model;

import org.betterave.enums.Role;
import org.jcrom.annotations.JcrName;
import org.jcrom.annotations.JcrNode;
import org.jcrom.annotations.JcrPath;
import org.jcrom.annotations.JcrProperty;

/**
 * Created by antoine on 14/06/15.
 */
@JcrNode(nodeType = "betterave:user")
public class User {

    public static final String ROOT_PATH = "/users";

    public static final String findByEmail(String email) {
        return "SELECT * FROM [betterave:user] WHERE email='" + email + "'";
    }

    @JcrPath
    private String path;

    @JcrName
    private String uuid;

    @JcrProperty
    private String password;

    @JcrProperty
    private String email;

    @JcrProperty
    private String lastName;

    @JcrProperty
    private String firstName;

    @JcrProperty
    private Role role;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
