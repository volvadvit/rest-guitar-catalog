package com.zuzex.vvolkov.model.guitar;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pickup")
@Getter
@Setter
@NoArgsConstructor
public class Pickup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PickupType type;

    public Pickup(String name, PickupType type) {
        this.name = name.toLowerCase();
        this.type = type;
    }
}
