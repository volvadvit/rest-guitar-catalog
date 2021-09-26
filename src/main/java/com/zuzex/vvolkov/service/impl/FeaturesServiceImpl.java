package com.zuzex.vvolkov.service.impl;

import com.zuzex.vvolkov.exceptions.InvalidInputParameterExceptions;
import com.zuzex.vvolkov.model.guitar.Features;
import com.zuzex.vvolkov.model.guitar.Pickup;
import com.zuzex.vvolkov.repositories.FeaturesRepo;
import com.zuzex.vvolkov.service.FeaturesService;
import com.zuzex.vvolkov.service.PickupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.*;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeaturesServiceImpl implements FeaturesService {

    private final FeaturesRepo featuresRepo;
    private final PickupService pickupServiceImpl;

    @Override
    public Features add(@Valid Features features) {
        Optional<Features> result = featuresRepo.findByBodyAndColourAndFretsNumberAndStringsNumberAndPickupsNumber(
                features.getBody(),
                features.getColour(),
                features.getFretsNumber(),
                features.getStringsNumber(),
                features.getPickupsNumber()
        );
        return result.orElseGet(() -> featuresRepo.save(features));
    }

    @Override
    public Features findFeaturesById(Long id) {
        Optional<Features> result = featuresRepo.findFeaturesById(id);
        if (result.isPresent()) {
            return result.get();
        }
        System.err.println("Features doesn't exists");
        return null;
    }

    public Features validate(Features features) {

        if (features != null) {
            Integer pickupsNumber = features.getPickupsNumber();
            if (pickupsNumber != null) {
                Pickup[] pickups = new Pickup[pickupsNumber];
                for (int i = 0; i < pickupsNumber; i++) {
                    Pickup pickup = features.getPickup(i);
                    if (pickup == null || pickup.getName() == null || pickup.getType() == null) {
                        throw new InvalidInputParameterExceptions("pickups");
                    }
                    pickups[i] = pickup;
                }

                Pickup[] pickupsResult = pickupServiceImpl.addPickups(pickups);
                for (int i = 0; i < pickupsResult.length; i++) {
                    features.setPickups(pickupsResult[i], i);
                }

                Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
                Set<ConstraintViolation<Features>> violations = validator.validate(features);

                if (!violations.isEmpty()) {
                    throw new ConstraintViolationException(violations);
                } else {
                    return this.add(features);
                }
            } else {
                throw new InvalidInputParameterExceptions("pickupsNumber");
            }
        } else {
            throw new InvalidInputParameterExceptions("features");
        }
    }
}
