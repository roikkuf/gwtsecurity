package com.gwt.ss.demo4.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 *
 * Code from Luke Taylor spring example
 */
@Service("openIdUserService")
public class OpenIdUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    private final Map<String, OpenIdUserDetails> registeredUsers = new HashMap<String, OpenIdUserDetails>();
    private static final List<GrantedAuthority> DEFAULT_AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_STAFF");
    private static final List<GrantedAuthority> ADMIN_AUTHORITIES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_STAFF");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserDetails user = registeredUsers.get(username);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return user;
    }

    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        String id = token.getIdentityUrl();

        OpenIdUserDetails user = registeredUsers.get(id);

        if (user != null) {
            return user;
        }

        String email = null;
        String firstName = null;
        String lastName = null;
        String fullName = null;

        List<OpenIDAttribute> attributes = token.getAttributes();

        for (OpenIDAttribute attribute : attributes) {
            if (attribute.getName().equals("email")) {
                email = attribute.getValues().get(0);
            }

            if (attribute.getName().equals("firstname")) {
                firstName = attribute.getValues().get(0);
            }

            if (attribute.getName().equals("lastname")) {
                lastName = attribute.getValues().get(0);
            }

            if (attribute.getName().equals("fullname")) {
                fullName = attribute.getValues().get(0);
            }
        }

        if (fullName == null) {
            StringBuilder fullNameBldr = new StringBuilder();

            if (firstName != null) {
                fullNameBldr.append(firstName);
            }

            if (lastName != null) {
                fullNameBldr.append(" ").append(lastName);
            }
            fullName = fullNameBldr.toString();
        }
        boolean isGUser = email!=null && email.endsWith("@gmail.com");
        user = new OpenIdUserDetails(id, isGUser ? ADMIN_AUTHORITIES : DEFAULT_AUTHORITIES);
        user.setEmail(email);
        user.setName(fullName);

        registeredUsers.put(id, user);

        user = new OpenIdUserDetails(id, isGUser ? ADMIN_AUTHORITIES : DEFAULT_AUTHORITIES);
        user.setEmail(email);
        user.setName(fullName);
        user.setNewUser(true);

        return user;
    }
}
