package com.zuzex.vvolkov.repositories;

import com.zuzex.vvolkov.model.guitar.Manufacturer;
import org.springframework.data.repository.CrudRepository;

public interface ManufacturerRepo extends CrudRepository<Manufacturer, Long> {
    Manufacturer findManufacturerById(Long id);
    Manufacturer findManufacturerByName(String name);
}
