package com.healoui.DailyAccountingServerSide.controller;

import com.healoui.DailyAccountingServerSide.exception.ResourceNotFoundException;
import com.healoui.DailyAccountingServerSide.models.DailyAccounting;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DailyAccountingController {

    public final DailyAccountingRepository dailyAccountingRepository;

    public final DailyAccountingResultsRepository dailyAccountingResultsRepository;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @GetMapping("/dailyAccounting")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllDailyAccountingPage(
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

            List<DailyAccounting> dailyAccountingList = new ArrayList<DailyAccounting>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<DailyAccounting> pageTuts;
            if (title == null)
                pageTuts = dailyAccountingRepository.findAll(pagingSort);
            else
                pageTuts = dailyAccountingRepository.findByTitleContaining(title, pagingSort);

            dailyAccountingList = pageTuts.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("dailyAccountingList", dailyAccountingList);
            response.put("currentPage", pageTuts.getNumber());
            response.put("totalItems", pageTuts.getTotalElements());
            response.put("totalPages", pageTuts.getTotalPages());

            return new ResponseEntity<>(response, HttpStatus.OK);

    }


    @GetMapping("/dailyAccounting/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DailyAccounting> getDailyAccountingById(@PathVariable("id") long id) {
        DailyAccounting dailyAccounting = dailyAccountingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DailyAccounting with id = " + id));

        return new ResponseEntity<>(dailyAccounting, HttpStatus.OK);
    }


    @PostMapping("/dailyAccounting")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<DailyAccounting> createTutorial(@RequestBody DailyAccounting dailyAccounting) {

            LocalDateTime currentLocalDateTime = LocalDateTime.now();

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);
            DailyAccounting _dailyAccounting = dailyAccountingRepository.save(new DailyAccounting(
                    "OperationDate is : " + formattedDateTime,

                    dailyAccounting.getRecipeToday(),
                    dailyAccounting.getBalancePreviousMonth(),
                    dailyAccounting.getOperationTreasuryAnterior(),

                    dailyAccounting.getOperationTreasuryToday(),
                    dailyAccounting.getOperationPreviousRegulation(),
                    dailyAccounting.getOperationRegulationToday(),

                    dailyAccounting.getPostCurrentAccount(),
                    dailyAccounting.getCreditExpected(),
                    dailyAccounting.getRateExpected(),

                    dailyAccounting.getOtherValues(),
                    dailyAccounting.getStatesRepartition(),
                    dailyAccounting.getMoneySpecies()));


        return new ResponseEntity<>(_dailyAccounting, HttpStatus.CREATED);
    }

    @PutMapping("/dailyAccounting/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<DailyAccounting> updateDailyAccounting(@PathVariable("id") long id, @RequestBody DailyAccounting dailyAccounting) {
        DailyAccounting _dailyAccounting = dailyAccountingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found DailyAccounting with id = " + id));

        _dailyAccounting.setTitle(dailyAccounting.getTitle());

        _dailyAccounting.setRecipeToday(dailyAccounting.getRecipeToday());
        _dailyAccounting.setBalancePreviousMonth(dailyAccounting.getBalancePreviousMonth());
        _dailyAccounting.setOperationTreasuryAnterior(dailyAccounting.getOperationTreasuryAnterior());

        _dailyAccounting.setOperationTreasuryToday(dailyAccounting.getOperationTreasuryToday());
        _dailyAccounting.setOperationPreviousRegulation(dailyAccounting.getOperationPreviousRegulation());
        _dailyAccounting.setOperationRegulationToday(dailyAccounting.getOperationRegulationToday());

        _dailyAccounting.setPostCurrentAccount(dailyAccounting.getPostCurrentAccount());
        _dailyAccounting.setCreditExpected(dailyAccounting.getRateExpected());
        _dailyAccounting.setRateExpected(dailyAccounting.getRateExpected());

        _dailyAccounting.setStatesRepartition(dailyAccounting.getStatesRepartition());
        _dailyAccounting.setOtherValues(dailyAccounting.getOtherValues());
        _dailyAccounting.setMoneySpecies(dailyAccounting.getMoneySpecies());

        return new ResponseEntity<>(dailyAccountingRepository.save(_dailyAccounting), HttpStatus.OK);
    }

    @DeleteMapping("/dailyAccounting/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteDailyAccounting(@PathVariable("id") long id) {
        if (dailyAccountingResultsRepository.existsById(id)) {
            dailyAccountingResultsRepository.deleteById(id);
        }

        dailyAccountingRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/dailyAccounting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllDailyAccounting() {
        dailyAccountingRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
