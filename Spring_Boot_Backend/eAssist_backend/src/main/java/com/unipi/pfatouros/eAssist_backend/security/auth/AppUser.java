package com.unipi.pfatouros.eAssist_backend.security.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class AppUser implements UserDetails {

    // This class is an implementation of the UserDetails interface

    private String username; // User's username

    private String email; // User's email

    @JsonIgnore
    private String password; // User's password

    private Collection<? extends GrantedAuthority> authorities; // User's authorities

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
