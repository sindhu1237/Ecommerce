package com.app.ecomapplication.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String city;
    private String street;
    private int number;
    private String zipcode;
    @Embedded
    private Geolocation geolocation;
}