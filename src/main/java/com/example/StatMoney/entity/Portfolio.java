package com.example.StatMoney.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "portfolios")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "portfolio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Asset> assets;

    private Double totalValueRub;               // Общая стоимость в рублях
    private Double totalValueUsd;               // Общая стоимость в долларах

    private Double totalProfitLossRub;          // Общий доход/убыток в рублях
    private Double totalProfitLossUsd;          // Общий доход/убыток в долларах

    private Double averageDailyIncomeRub;       // Среднедневной доход в рублях
    private Double averageDailyIncomeUsd;       // Среднедневной доход в долларах

    private Double totalSoldAssetsValueRub;     // Общая стоимость проданных активов в рублях
    private Double totalSoldAssetsValueUsd;     // Общая стоимость проданных активов в долларах
}
