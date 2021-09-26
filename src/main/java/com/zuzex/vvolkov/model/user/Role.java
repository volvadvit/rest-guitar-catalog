package com.zuzex.vvolkov.model.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    MANAGER;

    public String getAuthority() {
        return this.name();
    }
}

