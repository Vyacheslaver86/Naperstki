package com.naperstky.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class InterPlayerUserAccount extends UserAccount{


    private final UserAccount userAccount;

    public InterPlayerUserAccount(UserAccount userAccount){

        this.userAccount=userAccount;


    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
