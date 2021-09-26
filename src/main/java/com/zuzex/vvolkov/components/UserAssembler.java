package com.zuzex.vvolkov.components;

import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.model.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public UserDTO toUserVO(AppUser user) {
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getRoles(), user.getGuitars());
    }
}

