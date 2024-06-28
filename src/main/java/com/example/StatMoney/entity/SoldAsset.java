package com.example.StatMoney.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "soldAssets")
public class SoldAsset extends Asset{
    private Double sellPriceRub;
    private Double sellPriceUsd;
    private Integer sellQuantity;
    private Date sellDate;
    private Double profitLossRub;
    private Double profitLossUsd;
}
