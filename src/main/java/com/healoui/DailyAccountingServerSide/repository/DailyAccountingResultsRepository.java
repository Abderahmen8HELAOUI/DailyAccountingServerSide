package com.healoui.DailyAccountingServerSide.repository;

import com.healoui.DailyAccountingServerSide.models.DailyAccountingResults;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyAccountingResultsRepository extends JpaRepository<DailyAccountingResults, Long> {

    Page<DailyAccountingResults> findByTitleContaining(String title, Pageable pageable);

    @Transactional
    void deleteById(long id);

    @Transactional
    void deleteByDailyAccountingId(long tutorialId);

    @Query(value = "SELECT CAST(SUM(da.balance_previous_month + da.recipe_today) as decimal(10,3)) AS recette_total\n" +
            "\tFROM public.daily_accounting da\n" +
            "\tWHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double totalRecipe();

    @Query(value = "SELECT CAST(SUM(da.operation_treasury_anterior + da.operation_treasury_today) as decimal(10,3)) AS total_operation_tresor\n" +
            "\tFROM public.daily_accounting da\n" +
            "\tWHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double totalTreasuryOperations();

    @Query(value = "SELECT CAST(SUM(da.operation_regulation_prior + da.operation_regulation_today) as decimal(10,3)) AS total_operation_tresor\n" +
            "\tFROM public.daily_accounting da\n" +
            "\tWHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double totalOperationRegulationToday();

    @Query(value = "SELECT CAST(SUM(da.operation_treasury_anterior + da.operation_treasury_today +\n" +
            "\t\t\t\t\tda.operation_regulation_prior + da.operation_regulation_today) as decimal(10,3)) AS total_expenses\n" +
            "\tFROM public.daily_accounting da\n" +
            "\tWHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double totalExpensesToday();

    @Query(value = "SELECT CAST(((da.balance_previous_month+da.recipe_today)-(da.operation_treasury_anterior + da.operation_treasury_today +\n" +
            "\t\t\t\t\tda.operation_regulation_prior + da.operation_regulation_today)) as decimal(10,3)) AS total_expenses\n" +
            "\tFROM public.daily_accounting da\n" +
            "\tWHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double currentBalanceToday();

    @Query(value = "SELECT CAST(SUM((postal_current_account+credit_expected)-expected_flow) as decimal(10,3)) AS total_expenses\n" +
            "\tFROM public.daily_accounting da\n" +
            "\tWHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double finalPostalCurrentAccount();

    @Query(value = "SELECT CAST(((da.balance_previous_month+da.recipe_today)-\n" +
            "\t\t\t (da.operation_treasury_anterior + da.operation_treasury_today +da.operation_regulation_prior + da.operation_regulation_today))-\n" +
            "\t\t\t(states_repartition + other_values + ((postal_current_account+credit_expected)-expected_flow))\n" +
            "\t\t\tas decimal(10,3))\n" +
            "            FROM public.daily_accounting da\n" +
            "            WHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double totalCash();

    @Query(value = "SELECT CAST(((da.balance_previous_month+da.recipe_today)-\n" +
            "\t\t\t (da.operation_treasury_anterior + da.operation_treasury_today +da.operation_regulation_prior + da.operation_regulation_today))-\n" +
            "\t\t\t(states_repartition + other_values + ((postal_current_account+credit_expected)-expected_flow))-money_species\n" +
            "\t\t\tas decimal(10,3))\n" +
            "            FROM public.daily_accounting da\n" +
            "            WHERE TO_CHAR(current_date, 'dd-mm-yyyy') = SUBSTRING(da.title, 20, 10);", nativeQuery = true)
    public Double currencyCashOnCashier();
}
