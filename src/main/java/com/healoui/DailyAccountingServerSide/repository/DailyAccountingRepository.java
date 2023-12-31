package com.healoui.DailyAccountingServerSide.repository;

import com.healoui.DailyAccountingServerSide.models.DailyAccounting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DailyAccountingRepository extends JpaRepository<DailyAccounting, Long> {
    Page<DailyAccounting> findByTitleContaining(String title, Pageable pageable);

    List<DailyAccounting> findByTitleContaining(String title, Sort sort);

   }
