package com.zuzex.vvolkov.repositories;

import com.zuzex.vvolkov.model.user.AppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
}
