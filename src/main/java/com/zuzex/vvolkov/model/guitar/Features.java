package com.zuzex.vvolkov.model.guitar;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Features {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String body;
    private String colour;

    @NotNull
    @DecimalMin("19")
    @DecimalMax("27")
    private Integer fretsNumber;

    @NotNull
    @DecimalMin("4")
    @DecimalMax("12")
    private Integer stringsNumber;

    @NotNull
    @DecimalMin("0")
    @DecimalMax("3")
    private Integer pickupsNumber;

    @ManyToOne
    @JoinColumn(name = "first_pickup_id", referencedColumnName = "id")
    private Pickup firstPickup;

    @ManyToOne
    @JoinColumn(name = "second_pickup_id", referencedColumnName = "id")
    private Pickup secondPickup;

    @ManyToOne
    @JoinColumn(name = "third_pickup_id", referencedColumnName = "id")
    private Pickup thirdPickup;

    public Features(String body, String colour, Integer fretsNumber,
                    Integer stringsNumber, Integer pickupsNumber, Pickup firstPickup,
                    Pickup secondPickup)
    {
        this.body = body.toLowerCase();
        this.colour = colour.toLowerCase();
        this.fretsNumber = fretsNumber;
        this.stringsNumber = stringsNumber;
        this.pickupsNumber = pickupsNumber;
        this.firstPickup = firstPickup;
        this.secondPickup = secondPickup;
    }

    public Features(String body, String colour, Integer fretsNumber,
                    Integer stringsNumber, Integer pickupsNumber, Pickup firstPickup)
    {
        this.body = body.toLowerCase();
        this.colour = colour.toLowerCase();
        this.fretsNumber = fretsNumber;
        this.stringsNumber = stringsNumber;
        this.pickupsNumber = pickupsNumber;
        this.firstPickup = firstPickup;
    }

    public Features(String body, String colour, Integer fretsNumber,
                    Integer stringsNumber, Integer pickupsNumber)
    {
        this.body = body.toLowerCase();
        this.colour = colour.toLowerCase();
        this.fretsNumber = fretsNumber;
        this.stringsNumber = stringsNumber;
        this.pickupsNumber = pickupsNumber;
    }

    public Pickup getPickup(int index) {
        switch (index) {
            case 0 : return getFirstPickup();
            case 1 : return getSecondPickup();
            case 2 : return getThirdPickup();
            default: return null;
        }
    }

    public void setPickups(Pickup pickup, int index) {
        switch (index) {
            case 0 : this.setFirstPickup(pickup); break;
            case 1 : this.setSecondPickup(pickup); break;
            case 2 : this.setThirdPickup(pickup); break;
            default: break;
        }
    }
}
