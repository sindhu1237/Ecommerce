package com.app.ecomapplication.model;

import lombok.Data;

@Data
public class Address {
    private String city;
    private String street;
    private int number;
    private String zipcode;
    private Geolocation geolocation;
}
