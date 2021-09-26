package com.zuzex.vvolkov.service;

import com.zuzex.vvolkov.model.guitar.Guitar;
import com.zuzex.vvolkov.model.user.AppUser;

import java.util.List;
import java.util.Map;

public interface GuitarService {

    Guitar add(Guitar guitar);
    Guitar getById(Long id);
    Guitar validate(Guitar guitar);
    Map<String, List<Object>> compareGuitars(List<Long> guitarIds);

    List<AppUser> findUsersByGuitarId(Long id);

    List<Guitar> searchByParameters(
            String model,
            Double priceFrom,
            Double priceTo,
            Integer yearFrom,
            Integer yearTo,
            String type,
            String design,
            String body,
            String colour,
            Integer fretsNumberFrom,
            Integer fretsNumberTo,
            Integer stringsNumberFrom,
            Integer stringsNumberTo,
            Integer pickupsNumberFrom,
            Integer pickupsNumberTo,
            String manufacturer
    );
}
