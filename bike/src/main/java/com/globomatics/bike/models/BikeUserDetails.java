package com.globomatics.bike.models;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class BikeUserDetails extends org.springframework.security.core.userdetails.User {

    private String nickname;

    public BikeUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
