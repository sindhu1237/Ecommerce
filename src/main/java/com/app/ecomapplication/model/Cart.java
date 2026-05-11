package com.app.ecomapplication.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "carts")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cart {
    @Id
    private Long id;
    private Long userId;
    private LocalDate date;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    @ToString.Exclude
    private List<CartItem> products = new ArrayList<>();
}