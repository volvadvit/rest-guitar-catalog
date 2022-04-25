package com.zuzex.vvolkov.service.impl;

import com.zuzex.vvolkov.exceptions.GuitarNotExistsException;
import com.zuzex.vvolkov.exceptions.InvalidInputParameterExceptions;
import com.zuzex.vvolkov.exceptions.UserAlreadyExistsException;
import com.zuzex.vvolkov.exceptions.UserNotExistsException;
import com.zuzex.vvolkov.model.guitar.Guitar;
import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.model.user.Role;
import com.zuzex.vvolkov.repositories.UserRepo;
import com.zuzex.vvolkov.service.GuitarService;
import com.zuzex.vvolkov.service.UserService;
import com.zuzex.vvolkov.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final GuitarService guitarService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<Guitar> getGuitars(String username) {
        if (username != null) {
            Optional<AppUser> optional = userRepo.findByUsername(username);
            AppUser user = optional.orElseThrow(() -> {throw new UserNotExistsException(username);});

            return user.getGuitars();
        } else {
            throw new InvalidInputParameterExceptions("invalid username");
        }
    }

    @Override
    public AppUser addGuitarsToUserByAdmin(List<Long> guitarsId, String username) {
        if (guitarsId != null && username != null && !username.isEmpty()) {
            Optional<AppUser> optional = userRepo.findByUsername(username);
            AppUser user = optional.orElseThrow(() -> {throw new UserNotExistsException(username);});

            guitarsId.forEach(id -> {
                user.getGuitars().add(guitarService.getById(id));
            });
            return userRepo.save(user);
        } else {
            throw new InvalidInputParameterExceptions("invalid input parameter");
        }
    }

    @Override
    public List<AppUser> addAllUsers(List<AppUser> userList) {
        if (userList != null) {
            return (List<AppUser>) userRepo.saveAll(userList);
        } else {
            return null;
        }
    }

    @Override
    public AppUser addGuitarsToUserByUser(List<Long> guitarsId, String authorizationHeader) {

        String username = TokenUtils.getInstance().getUsernameFromToken(authorizationHeader);

        if (guitarsId != null && username != null && !username.isEmpty()) {
            Optional<AppUser> optional = userRepo.findByUsername(username);
            AppUser user = optional.orElseThrow(() -> {throw new UserNotExistsException(username);});

            guitarsId.forEach(id -> {
                if (guitarService.getById(id) != null) {
                    user.getGuitars().add(guitarService.getById(id));
                } else {
                    throw new GuitarNotExistsException("guitar: " + id + " doesn't exists");
                }
            });
            return userRepo.save(user);
        } else {
            throw new InvalidInputParameterExceptions("invalid input parameter");
        }
    }

    public AppUser addUser(AppUser user) {
        log.info("Saving user {} to the db", user.getUsername());

        if (user.getUsername() != null && user.getPassword() != null && !user.getUsername().isEmpty()) {
            if (userRepo.findByUsername(user.getUsername()).isEmpty()) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                return userRepo.save(user);
            } else {
                throw new UserAlreadyExistsException(user.getUsername());
            }
        } else {
            throw new InvalidInputParameterExceptions("username or password are unavailable");
        }
    }

    public AppUser addRoleToUser(String username, Role role) {
        log.info("Set role {} to the user {}", role, username);

        if (username != null && !username.isEmpty() && role != null) {
            AppUser user = userRepo.findByUsername(username).orElseThrow(() -> {throw new UserNotExistsException(username);});
            user.getRoles().add(role);
            return userRepo.save(user);
        }  else {
            throw new InvalidInputParameterExceptions("username or role are unavailable");
        }
    }

    public AppUser getUser(String username) {
        log.info("Get user {}", username);

        if (username != null && !username.isEmpty()) {
            Optional<AppUser> optional = userRepo.findByUsername(username);
            return optional.orElseThrow(() -> {throw new UserNotExistsException(username);});
        } else {
            throw new InvalidInputParameterExceptions("username are null or empty");
        }
    }

    public List<AppUser> getAllUsers() {
        return (List<AppUser>) userRepo.findAll();
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("load user {}", username);

        Optional<AppUser> optional = userRepo.findByUsername(username);
            AppUser user = optional.orElseThrow(() -> {throw new UserNotExistsException(username);});
            return new User(user.getUsername(), user.getPassword(), user.getRoles());
    }
}