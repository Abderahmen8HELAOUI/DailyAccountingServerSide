package com.healoui.DailyAccountingServerSide.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dailyAccountingResults")
public class DailyAccountingResults {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "totalRecipeToday")
    private double totalRecipeToday;

    @Column(name = "totalOperationTreasuryToday")
    private double totalOperationTreasuryToday;

    @Column(name = "totalOperationRegulationToday")
    private double totalOperationRegulationToday;

    @Column(name = "totalExpensesToday")
    private double totalExpensesToday;

    @Column(name = "currentBalanceToday")
    private double currentBalanceToday;

    @Column(name = "finalPostalCurrentAccount")
    private double finalPostalCurrentAccount;

    @Column(name = "totalCash")
    private double totalCash;

    @Column(name = "currencyCashOnCashier")
    private double currencyCashOnCashier;

    @OneToOne
    @JoinColumn(name = "dailyAccounting_id", referencedColumnName = "id")
    private DailyAccounting dailyAccounting;

    @CreatedDate
    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModified;


    @CreatedBy
    @Column(
            nullable = false,
            updatable = false
    )
    private String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    private String lastModifiedBy;

    public DailyAccountingResults(String title,
                                  double totalRecipeToday,
                                  double totalOperationTreasuryToday,
                                  double totalOperationRegulationToday,
                                  double totalExpensesToday,
                                  double currentBalanceToday,
                                  double finalPostalCurrentAccount,
                                  double totalCash,
                                  double currencyCashOnCashier) {
        this.title =  title;
        this.totalRecipeToday = totalRecipeToday;
        this.totalOperationTreasuryToday = totalOperationTreasuryToday;
        this.totalOperationRegulationToday = totalOperationRegulationToday;
        this.totalExpensesToday = totalExpensesToday;
        this.currentBalanceToday = currentBalanceToday;
        this.finalPostalCurrentAccount = finalPostalCurrentAccount;
        this.totalCash = totalCash;
        this.currencyCashOnCashier = currencyCashOnCashier;
    }
}
