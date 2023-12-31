package com.healoui.DailyAccountingServerSide.repository;

import com.healoui.DailyAccountingServerSide.models.ERole;
import com.healoui.DailyAccountingServerSide.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
