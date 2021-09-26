package com.zuzex.vvolkov.service;

import com.zuzex.vvolkov.model.guitar.Guitar;
import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.model.user.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<AppUser> getAllUsers();
    AppUser getUser(String username);
    List<Guitar> getGuitars(String username);

    AppUser addUser(AppUser appUser);
    List<AppUser> addAllUsers(List<AppUser> userList);
    AppUser addRoleToUser(String username, Role role);
    AppUser addGuitarsToUserByAdmin(List<Long> guitarsId, String username);
    AppUser addGuitarsToUserByUser(List<Long> guitarsId, String authorizationHeader);
}
