package com.healoui.DailyAccountingServerSide.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dailyAccounting")
public class DailyAccounting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "title")
    private String title;

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
}
