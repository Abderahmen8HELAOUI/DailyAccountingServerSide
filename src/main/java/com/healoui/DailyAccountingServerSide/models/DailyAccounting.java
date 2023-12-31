package com.healoui.DailyAccountingServerSide.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
@Table(name = "dailyAccounting")
public class DailyAccounting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "organismCode")
    private String organismCode;


    @Column(name = "recipeToday")
    private double recipeToday;

    @Column(name = "balancePreviousMonth")
    private double balancePreviousMonth;

    @Column(name = "operationTreasuryAnterior")
    private double operationTreasuryAnterior;

    @Column(name = "operationTreasuryToday")
    private double operationTreasuryToday;

    @Column(name = "operationRegulationPrior")
    private double operationPreviousRegulation;

    @Column(name = "operationRegulationToday")
    private double operationRegulationToday;

    @Column(name = "postalCurrentAccount")
    private double postCurrentAccount;

    @Column(name = "creditExpected")
    private double creditExpected;

    @Column(name = "expectedFlow")
    private double rateExpected;

    @Column(name = "otherValues")
    private double otherValues;

    @Column(name = "statesRepartition")
    private double statesRepartition;

    @Column(name = "moneySpecies")
    private double moneySpecies;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    //@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organism_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Organism organism;

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

    public DailyAccounting(String title, double recipeToday, double balancePreviousMonth,
                           double operationTreasuryAnterior, double operationTreasuryToday,
                           double operationPreviousRegulation, double operationRegulationToday,
                           double postCurrentAccount, double creditExpected, double rateExpected, double otherValues,
                           double statesRepartition, double moneySpecies) {
        this.title = title;
        this.recipeToday = recipeToday;
        this.balancePreviousMonth = balancePreviousMonth;
        this.operationTreasuryAnterior = operationTreasuryAnterior;
        this.operationTreasuryToday = operationTreasuryToday;
        this.operationPreviousRegulation = operationPreviousRegulation;
        this.operationRegulationToday = operationRegulationToday;
        this.postCurrentAccount = postCurrentAccount;
        this.creditExpected = creditExpected;
        this.rateExpected = rateExpected;
        this.otherValues = otherValues;
        this.statesRepartition = statesRepartition;
        this.moneySpecies = moneySpecies;
    }
}
