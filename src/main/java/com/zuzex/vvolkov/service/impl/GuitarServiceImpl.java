package com.zuzex.vvolkov.service.impl;

import com.zuzex.vvolkov.exceptions.GuitarNotExistsException;
import com.zuzex.vvolkov.exceptions.InvalidInputParameterExceptions;
import com.zuzex.vvolkov.model.guitar.Features;
import com.zuzex.vvolkov.model.guitar.Guitar;
import com.zuzex.vvolkov.model.guitar.Manufacturer;
import com.zuzex.vvolkov.model.user.AppUser;
import com.zuzex.vvolkov.repositories.FeaturesRepo;
import com.zuzex.vvolkov.repositories.GuitarRepo;
import com.zuzex.vvolkov.repositories.ManufacturerRepo;
import com.zuzex.vvolkov.service.GuitarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GuitarServiceImpl implements GuitarService {

    private final GuitarRepo guitarRepo;
    private final FeaturesRepo featuresRepo;
    private final ManufacturerRepo manufacturerRepo;

    public List<Guitar> searchByParameters(
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
            String manufacturerName
    ) {
        List<Guitar> resultList = new ArrayList<>();

        List<Guitar> guitarList = guitarRepo.findByParameters(
                model == null ? "%" : model,
                priceFrom == null ? 0.0 : priceFrom,
                priceTo == null ? Double.MAX_VALUE : priceTo,
                yearFrom == null ? 0 : yearFrom,
                yearTo == null ? 2021 : yearTo,
                type == null ? "%" : type,
                design == null ? "%" : design
        );

        List<Features> featuresList = featuresRepo.findByParameters(
                body == null ? "%" : body,
                colour == null ? "%" : colour,
                 fretsNumberFrom == null ? 19 : fretsNumberFrom,
                 fretsNumberTo == null ? 27 : fretsNumberTo,
                 stringsNumberFrom == null ? 4 : stringsNumberFrom,
                 stringsNumberTo == null ? 12 : stringsNumberTo,
                 pickupsNumberFrom == null ? 0 : pickupsNumberFrom,
                 pickupsNumberTo == null ? 3 : pickupsNumberTo
        );

        List<Manufacturer> manufacturerList = new ArrayList<>();
        if (manufacturerName == null) {
            manufacturerList = (List<Manufacturer>) manufacturerRepo.findAll();
        } else {
            manufacturerList.add(manufacturerRepo.findManufacturerByName(manufacturerName));
        }

        for (Guitar guitar : guitarList) {
            for (Features features : featuresList) {
                for (Manufacturer manufacturer : manufacturerList) {
                    if (guitar.getFeatures().getId().equals(features.getId()) &&
                            guitar.getManufacturer().getId().equals(manufacturer.getId()))
                    {
                        resultList.add(guitar);
                    }
                }
            }
        }

        return resultList;
    }

    @Override
    public List<AppUser> findUsersByGuitarId(Long id) {
        if (id != null) {
            Optional<Guitar> optional = guitarRepo.findById(id);
            if (optional.isPresent()) {
                return optional.get().getUsers();
            } else {
                throw new GuitarNotExistsException("guitar: " + id + " doesn't exists");
            }
        } else {
            throw new InvalidInputParameterExceptions("invalid guitar id");
        }
    }

    @Override
    public Guitar getById(Long id) {
        if (id != null) {
            Optional<Guitar> response = guitarRepo.findById(id);
            if (response.isPresent()) {
                return response.get();
            } else {
                throw new GuitarNotExistsException(
                        "Guitar with id " + id + " doesn't exist");
            }
        } else {
            throw new InvalidInputParameterExceptions("invalid guitar id");
        }
    }

    @Override
    public Guitar add(@Valid Guitar guitar) {
        return guitarRepo.save(guitar);
    }

    public Guitar validate(Guitar guitar) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Guitar>> violations = validator.validate(guitar);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        } else {
            return guitar;
        }
    }

    public Map<String, List<Object>> compareGuitars(List<Long> guitarIds) {
        Map<String, List<Object>> compareMap = new HashMap<>(Map.ofEntries(
                Map.entry("model", new ArrayList<>()),
                Map.entry("price", new ArrayList<>()),
                Map.entry("year", new ArrayList<>()),
                Map.entry("body", new ArrayList<>()),
                Map.entry("colour", new ArrayList<>()),
                Map.entry("manufacturer", new ArrayList<>()),
                Map.entry("fretsNumber", new ArrayList<>()),
                Map.entry("stringsNumber", new ArrayList<>()),
                Map.entry("pickupsNumber", new ArrayList<>()),
                Map.entry("firstPickup", new ArrayList<>()),
                Map.entry("secondPickup", new ArrayList<>()),
                Map.entry("thirdPickup", new ArrayList<>())
        ));

        final List<Guitar> guitarList = new ArrayList<>();

        guitarIds.stream().distinct().forEach(id -> {
            Optional<Guitar> result = guitarRepo.findById(id);
            if (result.isPresent()) {
                guitarList.add(result.get());
            } else {
                throw new GuitarNotExistsException("guitar: " + id + " doesn't exists");
            }
        });

        if (guitarList.size() != 1) {
            for (Guitar guitar : guitarList) {
                compareMap.get("model").add(guitar.getModel());
                compareMap.get("price").add(guitar.getPrice());
                compareMap.get("year").add(guitar.getYear());
                compareMap.get("body").add(guitar.getFeatures().getBody());
                compareMap.get("colour").add(guitar.getFeatures().getColour());
                compareMap.get("manufacturer").add(guitar.getManufacturer().getName());
                compareMap.get("fretsNumber").add(guitar.getFeatures().getFretsNumber());
                compareMap.get("stringsNumber").add(guitar.getFeatures().getStringsNumber());
                compareMap.get("pickupsNumber").add(guitar.getFeatures().getPickupsNumber());
                compareMap.get("firstPickup").add(guitar.getFeatures().getFirstPickup());
                compareMap.get("secondPickup").add(guitar.getFeatures().getSecondPickup());
                compareMap.get("thirdPickup").add(guitar.getFeatures().getThirdPickup());
            }

            for (Map.Entry<String, List<Object>> entry : compareMap.entrySet()) {
                List<Object> filterList = entry.getValue().stream().distinct()
                        .collect(Collectors.toList());
                if (!filterList.isEmpty()) {
                    compareMap.put(entry.getKey(), filterList);
                }
            }
        }
        return compareMap;
    }
}
