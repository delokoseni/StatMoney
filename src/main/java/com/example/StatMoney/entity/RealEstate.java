package com.example.StatMoney.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "realEstates")
public class RealEstate extends Asset{
    private Double rentalIncomeMonthly;         // Ежемесячный доход от аренды
    private String address;
    private Date purchaseDate;
}
