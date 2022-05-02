package com.zuzex.vvolkov.controller;

import com.zuzex.vvolkov.components.UserAssembler;
import com.zuzex.vvolkov.constants.ResponseMapper;
import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.service.UserService;
import com.zuzex.vvolkov.utils.TokenUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserAssembler userAssembler;

    @GetMapping("/all")
    public ResponseEntity<ResponseMapper> getUsers() {
        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "list of users", userService.getAllUsers()
                .stream().map(userAssembler::toUserVO).collect(Collectors.toList())));
    }

    @PostMapping("/save")
    @ApiOperation(value = "Method to register/save user")
    public ResponseEntity<ResponseMapper> saveUser(@RequestBody @Validated AppUser appUser) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.created(uri).body(new ResponseMapper(HttpStatus.CREATED.value(),
                "user added", userAssembler.toUserVO(this.userService.addUser(appUser))));
    }

    @GetMapping("/token/refresh")
    @ApiOperation("Method to generate new pair of access/refresh tokens")
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return TokenUtils.getInstance().updateAccessToken(request.getHeader(AUTHORIZATION), userService);
    }

    @GetMapping("/{username}/guitars")
    @ApiOperation("Get guitars list from bag of specified user")
    public ResponseEntity<ResponseMapper> getUserGuitars(@PathVariable("username") String username) {
        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "list of user guitars", userService.getGuitars(username)));
    }

    @PostMapping("/guitars/add")
    @ApiOperation("Add guitar to bag of currently logged user")
    public ResponseEntity<ResponseMapper> addGuitarsToUserByUser(
            @RequestBody List<Long> guitarsId, HttpServletRequest request)
    {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        return ResponseEntity.ok(new ResponseMapper(HttpStatus.OK.value(),
                "guitars added to the user", userService.addGuitarsToUserByUser(guitarsId, authorizationHeader)));
    }
}