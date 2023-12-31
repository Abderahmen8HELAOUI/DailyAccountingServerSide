package com.healoui.DailyAccountingServerSide.repository;


import com.healoui.DailyAccountingServerSide.models.Organism;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganismRepository extends JpaRepository<Organism, Long> {
}
