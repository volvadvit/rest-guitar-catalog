package com.zuzex.vvolkov.service.impl;

import com.zuzex.vvolkov.model.guitar.Manufacturer;
import com.zuzex.vvolkov.repositories.ManufacturerRepo;
import com.zuzex.vvolkov.service.ManufacturerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@RequiredArgsConstructor
public class ManufacturerServiceImpl implements ManufacturerService {

    private final ManufacturerRepo manufacturerRepo;

    @Override
    public Manufacturer getManufacturerById(Long id) {
        return manufacturerRepo.findManufacturerById(id);
    }

    @Override
    public Manufacturer add(@Valid Manufacturer manufacturer) {
        Manufacturer manufacturerByName = manufacturerRepo.findManufacturerByName(manufacturer.getName());
        if (manufacturerByName != null) {
            if (manufacturerByName.getId() != null) {
                return manufacturerRepo.findById(manufacturerByName.getId()).get();
            }
        }
        return manufacturerRepo.save(manufacturer);
    }

    public Manufacturer getManufacturerByName(String name) {
        return manufacturerRepo.findManufacturerByName(name);
    }
}
