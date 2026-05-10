package com.app.ecomapplication.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private Address address;
    private String phone;
}
