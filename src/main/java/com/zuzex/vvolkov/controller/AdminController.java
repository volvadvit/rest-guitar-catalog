package com.zuzex.vvolkov.controller;

import com.zuzex.vvolkov.constants.ResponseMapper;
import com.zuzex.vvolkov.model.guitar.Guitar;
import com.zuzex.vvolkov.model.user.RoleToUsernameForm;
import com.zuzex.vvolkov.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @PostMapping("/guitars/add")
    public ResponseEntity<ResponseMapper> addGuitarsToUserByAdmin(
            @RequestBody List<Long> guitarsId, @RequestParam String username)
    {
        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "guitars added to the user", userService.addGuitarsToUserByAdmin(guitarsId, username)));
    }

    @PostMapping("/role/update")
    public ResponseEntity<ResponseMapper> updateUserRole(@RequestBody RoleToUsernameForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRole());
        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "role added", userService.getUser(form.getUsername())));
    }
}
