package com.example.StatMoney.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "preciousMetals")
public class PreciousMetal extends Asset{
    private String metalType;
    private Double quantityGr;
    private Date purchaseDate;
}
