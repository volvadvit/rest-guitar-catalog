package com.zuzex.vvolkov.model.guitar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zuzex.vvolkov.model.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Guitar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String model;

    @DecimalMax("2021")
    private Integer year;

    @DecimalMin("0.0")
    @NotNull
    private Double price;

    @NotNull
    private String photoUrl;

    private String description;

    @ManyToOne
    @JoinColumn(name = "features_id", referencedColumnName = "id")
    private Features features;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Design design;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "id")
    private Manufacturer manufacturer;

    @JsonIgnore
    @ManyToMany(mappedBy = "guitars", targetEntity = AppUser.class, fetch = FetchType.EAGER)
    List<AppUser> users;

    public Guitar(String model, Integer year, Double price,
                  String photoUrl, String description,
                  Features features, Type type, Design design,
                  Manufacturer manufacturer)
    {
        this.model = model.toLowerCase();
        this.year = year;
        this.price = price;
        this.photoUrl = photoUrl;
        this.description = description.toLowerCase();
        this.features = features;
        this.type = type;
        this.design = design;
        this.manufacturer = manufacturer;
    }
}
