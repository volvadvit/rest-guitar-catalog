package com.zuzex.vvolkov.repositories;

import com.zuzex.vvolkov.model.guitar.Pickup;
import com.zuzex.vvolkov.model.guitar.PickupType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PickupRepo extends CrudRepository<Pickup, Long> {
    List<Pickup> findByName(String name);
    List<Pickup> findByType(PickupType type);
    Optional<Pickup> findByNameAndType(String name, PickupType type);
}
