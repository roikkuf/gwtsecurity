package com.gwt.ss.demo4.server;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * Code from Luke Taylor  spring example
 */
public class OpenIdUserDetails extends User {

    private String email;
    private String name;
    private boolean newUser;

    public OpenIdUserDetails(String username, Collection<GrantedAuthority> authorities) {
        super(username, "unused", true, true, true, true, authorities);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
