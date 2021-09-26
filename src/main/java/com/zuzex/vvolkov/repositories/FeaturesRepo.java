package com.zuzex.vvolkov.repositories;

import com.zuzex.vvolkov.model.guitar.Features;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeaturesRepo extends CrudRepository<Features, Long> {
    Optional<Features> findFeaturesById(Long id);
    Optional<Features> findByBodyAndColourAndFretsNumberAndStringsNumberAndPickupsNumber(
            String body,
            String colour,
            Integer fretsNumber,
            Integer stringsNumber,
            Integer pickupsNumber);

    @Query(value = "SELECT * FROM features f WHERE " +
            "f.body LIKE :body AND " +
            "f.colour LIKE :colour AND " +
            "f.frets_number BETWEEN :fretsNumber_from AND :fretsNumber_to AND " +
            "f.strings_number BETWEEN :stringsNumber_from AND :stringsNumber_to AND " +
            "f.pickups_number BETWEEN :pickupsNumber_from AND :pickupsNumber_to"
            , nativeQuery = true)
    List<Features> findByParameters(
            @Param("body") String body,
            @Param("colour") String colour,
            @Param("fretsNumber_from") Integer fretsNumberFrom,
            @Param("fretsNumber_to") Integer fretsNumberTo,
            @Param("stringsNumber_from") Integer stringsNumberFrom,
            @Param("stringsNumber_to") Integer stringsNumberTo,
            @Param("pickupsNumber_from") Integer pickupsNumberFrom,
            @Param("pickupsNumber_to") Integer pickupsNumberTo
    );
}
