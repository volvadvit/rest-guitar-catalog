package com.zuzex.vvolkov.model.user;

import com.zuzex.vvolkov.model.guitar.Guitar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Getter
@Setter
public class UserDTO extends AppUser {

    private Long id;
    private String name;
    private String username;
    private Set<Role> roles;
    private List<Guitar> guitars;
}
