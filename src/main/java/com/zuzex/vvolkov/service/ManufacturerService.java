package com.zuzex.vvolkov.service;

import com.zuzex.vvolkov.model.guitar.Manufacturer;

public interface ManufacturerService {
    Manufacturer add(Manufacturer manufacturer);
    Manufacturer getManufacturerById(Long id);
}
