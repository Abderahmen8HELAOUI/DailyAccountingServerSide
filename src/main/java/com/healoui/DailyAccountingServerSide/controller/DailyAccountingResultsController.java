package com.healoui.DailyAccountingServerSide.controller;

import com.healoui.DailyAccountingServerSide.exception.ResourceNotFoundException;
import com.healoui.DailyAccountingServerSide.models.DailyAccounting;
import com.healoui.DailyAccountingServerSide.models.DailyAccountingResults;
import com.healoui.DailyAccountingServerSide.repository.DailyAccountingRepository;
import com.healoui.DailyAccountingServerSide.repository.DailyAccountingResultsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DailyAccountingResultsController {

    public final DailyAccountingResultsRepository dailyAccountingResultsRepository;

    public final DailyAccountingRepository dailyAccountingRepository;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @GetMapping("/dailyAccountingResults")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllDailyAccountingResultsPage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        List<Sort.Order> orders = new ArrayList<Sort.Order>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        List<DailyAccountingResults> dailyAccountingResultsList = new ArrayList<DailyAccountingResults>();
        Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

        Page<DailyAccountingResults> pageTuts;
        if (title == null)
            pageTuts = dailyAccountingResultsRepository.findAll(pagingSort);
        else
            pageTuts = dailyAccountingResultsRepository.findByTitleContaining(title, pagingSort);

        dailyAccountingResultsList = pageTuts.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("dailyAccountingResultsList", dailyAccountingResultsList);
        response.put("currentPage", pageTuts.getNumber());
        response.put("totalItems", pageTuts.getTotalElements());
        response.put("totalPages", pageTuts.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping({ "/results/{id}", "/dailyAccounting/{id}/results" })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DailyAccountingResults> getResultsById(@PathVariable(value = "id") Long id) {
        DailyAccountingResults results = dailyAccountingResultsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DailyAccounting Results with id = " + id));

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/dailyAccounting/{dailyAccountingId}/results")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<DailyAccountingResults> createResults(@PathVariable(value = "dailyAccountingId") Long dailyAccountingId,
                                                                @RequestBody DailyAccountingResults resultsRequest) {
        DailyAccounting dailyAccounting = dailyAccountingRepository.findById(dailyAccountingId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DailyAccounting with id = " + dailyAccountingId));

        resultsRequest.setTitle("the DailyAccounting Id is: " + Long.toString(dailyAccounting.getId()));
        resultsRequest.setTotalRecipeToday(dailyAccountingResultsRepository.totalRecipe());
        resultsRequest.setTotalOperationTreasuryToday(dailyAccountingResultsRepository.totalTreasuryOperations());
        resultsRequest.setTotalOperationRegulationToday(dailyAccountingResultsRepository.totalOperationRegulationToday());
        resultsRequest.setTotalExpensesToday(dailyAccountingResultsRepository.totalExpensesToday());
        resultsRequest.setCurrentBalanceToday(dailyAccountingResultsRepository.currentBalanceToday());
        resultsRequest.setFinalPostalCurrentAccount(dailyAccountingResultsRepository.finalPostalCurrentAccount());
        resultsRequest.setTotalCash(dailyAccountingResultsRepository.totalCash());
        resultsRequest.setCurrencyCashOnCashier(dailyAccountingResultsRepository.currencyCashOnCashier());

        resultsRequest.setDailyAccounting(dailyAccounting);

        DailyAccountingResults results = dailyAccountingResultsRepository.save(resultsRequest);

        return new ResponseEntity<>(results, HttpStatus.CREATED);
    }

    @PutMapping("/results/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<DailyAccountingResults> updateResults(@PathVariable("id") long id,
                                                         @RequestBody DailyAccountingResults resultsRequest) {
        DailyAccountingResults results = dailyAccountingResultsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));

        results.setTotalRecipeToday(dailyAccountingResultsRepository.totalRecipe());
        results.setTotalOperationTreasuryToday(dailyAccountingResultsRepository.totalTreasuryOperations());
        results.setTotalOperationRegulationToday(dailyAccountingResultsRepository.totalOperationRegulationToday());
        results.setTotalExpensesToday(dailyAccountingResultsRepository.totalExpensesToday());
        results.setCurrentBalanceToday(dailyAccountingResultsRepository.currentBalanceToday());
        results.setFinalPostalCurrentAccount(dailyAccountingResultsRepository.finalPostalCurrentAccount());
        results.setTotalCash(dailyAccountingResultsRepository.totalCash());
        results.setCurrencyCashOnCashier(dailyAccountingResultsRepository.currencyCashOnCashier());

        return new ResponseEntity<>(dailyAccountingResultsRepository.save(results), HttpStatus.OK);
    }

    @DeleteMapping("/results/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteResults(@PathVariable("id") long id) {
        dailyAccountingResultsRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/dailyAccounting/{dailyAccountingId}/results")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DailyAccountingResults> deleteResultsOfDailyAccounting(@PathVariable(value = "dailyAccountingId") Long dailyAccountingId) {
        if (!dailyAccountingRepository.existsById(dailyAccountingId)) {
            throw new ResourceNotFoundException("Not found DailyAccounting with id = " + dailyAccountingId);
        }

        dailyAccountingResultsRepository.deleteByDailyAccountingId(dailyAccountingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
