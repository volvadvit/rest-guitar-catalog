package com.zuzex.vvolkov.service;

import com.zuzex.vvolkov.model.guitar.Features;
import com.zuzex.vvolkov.model.guitar.Pickup;

import java.util.List;
import java.util.Map;

public interface PickupService {

    Pickup add(Pickup pickup);
    Pickup getPickupById(Long id);
    Pickup[] addPickups(Pickup[] pickups);

    Map<String, List<Object>> comparePickupsProperties(Features first, Features second);
}
