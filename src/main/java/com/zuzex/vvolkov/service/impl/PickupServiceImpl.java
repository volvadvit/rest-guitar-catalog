package com.zuzex.vvolkov.service.impl;

import com.zuzex.vvolkov.model.guitar.Features;
import com.zuzex.vvolkov.model.guitar.Pickup;
import com.zuzex.vvolkov.repositories.PickupRepo;
import com.zuzex.vvolkov.service.PickupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PickupServiceImpl implements PickupService {

    private final PickupRepo pickupRepo;

    @Override
    public Pickup add(@Valid Pickup pickup) {
        Optional<Pickup> result = pickupRepo.findByNameAndType(pickup.getName(), pickup.getType());
        return result.orElseGet(() -> pickupRepo.save(pickup));
    }

    @Override
    public Pickup getPickupById(Long id) {
        Optional<Pickup> result = pickupRepo.findById(id);
        if (result.isPresent()) {
            return result.get();
        } else {
           throw new RuntimeException("Pickup doesn't exists");
        }
    }

    @Override
    public Map<String, List<Object>> comparePickupsProperties(Features first, Features second) {
        Map<String, List<Object>> map = new HashMap<>();

        Pickup[] firstPickups = new Pickup[3];
        Pickup[] secondPickups = new Pickup[3];

        for (int i = 0; i < 3; i++) {
            firstPickups[i] = first.getPickup(i);
            secondPickups[i] = second.getPickup(i);

            if (firstPickups[i] != null) {
                if (secondPickups[i] != null) {
                    map.put("pickupsName", Arrays.asList(firstPickups[i].getName(), secondPickups[i].getName()));
                } else {
                    map.put("pickupsName", Arrays.asList(firstPickups[i].getName(), ""));
                }
            } else if (secondPickups[i] != null) {
                map.put("pickupsName", Arrays.asList("", secondPickups[i].getName()));
            } else {
                break;
            }
        }
        return map;
    }

    @Override
    public Pickup[] addPickups(Pickup[] pickups) {

        Pickup[] response = new Pickup[pickups.length];

        for (int i = 0; i < pickups.length; i++) {
            Pickup pickup = pickups[i];
            if (pickup != null) {
                Optional<Pickup> result = pickupRepo.findByNameAndType(pickup.getName(), pickup.getType());
                response[i] = result.orElseGet(() -> pickupRepo.save(pickup));
            } else {
                break;
            }
        }
        return response;
    }
}
