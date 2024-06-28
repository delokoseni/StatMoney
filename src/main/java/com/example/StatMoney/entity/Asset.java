package com.example.StatMoney.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)   // Наследование ьазовых атрибутов подклассами
@Table(name="assets")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idPortfolio")
    private Portfolio portfolio;

    private String name;
    private String category;
    private Double purchasePriceRub;             // Цена покупки в рублях
    private Double purchasePriceUsd;             // Цена покупки в долларах
    private Integer quantity;                    // Количество
}
