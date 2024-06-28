package com.example.StatMoney.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cryptocurrency")
public class Cryptocurrency extends Asset{
    private String ticker;
}
