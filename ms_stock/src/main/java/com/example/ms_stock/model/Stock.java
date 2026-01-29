package com.example.ms_stock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name is required.")
    private String nom;
    @ElementCollection
    @CollectionTable(
            name = "stock_magasinier",
            joinColumns = @JoinColumn(name = "stock_id")
    )
    @Column(name = "User_id")
    private List<Long> UserIds;
}
