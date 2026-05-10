package com.app.ecomapplication.model;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Name {

    private String firstname;

    private String lastname;
}