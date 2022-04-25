package com.zuzex.vvolkov;

import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.model.user.Role;
import com.zuzex.vvolkov.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;

@SpringBootApplication
public class StartApplication {
    public static void main(String[] args) {
        SpringApplication.run(StartApplication.class, args);
    }


//	@Bean
//	public CommandLineRunner run(UserService userService) {
//		return args -> {
//
//			userService.addUser(new AppUser("John", "john123", "1234", new HashSet<Role>()));
//			userService.addUser(new AppUser("Ben", "ben123", "1234",  new HashSet<Role>()));
//			userService.addUser(new AppUser("Ross", "ross123", "1234", new HashSet<Role>()));
//
//			userService.addRoleToUser("john123", Role.USER);
//
//			userService.addRoleToUser("ben123", Role.MANAGER);
//			userService.addRoleToUser("ben123", Role.USER);
//
//			userService.addRoleToUser("ross123", Role.ADMIN);
//			userService.addRoleToUser("ross123", Role.USER);
//			userService.addRoleToUser("ross123", Role.MANAGER);
//		};
//	}
}