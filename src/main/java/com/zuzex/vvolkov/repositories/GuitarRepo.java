package com.zuzex.vvolkov.repositories;

import com.zuzex.vvolkov.model.guitar.Guitar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuitarRepo extends CrudRepository<Guitar, Long> {

    @Query(value = "SELECT * FROM guitar g WHERE " +
            "g.model LIKE :model AND " +
            "g.price BETWEEN :price_from AND :price_to AND " +
            "g.year BETWEEN :year_from AND :year_to AND " +
            "g.type LIKE :type AND " +
            "g.design LIKE :design", nativeQuery = true)
    List<Guitar> findByParameters(
                                  @Param("model") String model,
                                  @Param("price_from") Double priceFrom,
                                  @Param("price_to") Double priceTo,
                                  @Param("year_from") Integer yearFrom,
                                  @Param("year_to") Integer yearTo,
                                  @Param("type") String type,
                                  @Param("design") String design
    );
}
