package com.zuzex.vvolkov.model.user;


import lombok.Data;

@Data
public class RoleToUsernameForm {
    private String username;
    private Role role;
}