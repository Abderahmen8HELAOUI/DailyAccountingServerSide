package com.healoui.DailyAccountingServerSide.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@Table(name = "organism")
@EntityListeners(AuditingEntityListener.class)
public class Organism {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organism_generator")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

}
