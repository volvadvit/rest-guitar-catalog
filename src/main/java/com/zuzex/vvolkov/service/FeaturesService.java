package com.zuzex.vvolkov.service;

import com.zuzex.vvolkov.model.guitar.Features;

public interface FeaturesService {
    Features add(Features features);
    Features findFeaturesById(Long id);
    Features validate(Features features);
}
