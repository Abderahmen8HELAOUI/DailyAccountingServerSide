package com.healoui.DailyAccountingServerSide.controller;

import com.healoui.DailyAccountingServerSide.models.DailyAccounting;
import com.healoui.DailyAccountingServerSide.payload.request.DailyAccountingRequest;
import com.healoui.DailyAccountingServerSide.repository.DailyAccountingRepository;
import com.healoui.DailyAccountingServerSide.security.sevices.DailyAccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DailyAccountingController {

    private final DailyAccountingRepository dailyAccountingRepository;

    private final JdbcTemplate jdbcTemplate;

    private final DailyAccountingService dailyAccountingService;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }

        return Sort.Direction.ASC;
    }

    @GetMapping("/sortedDailyAccounting")
    public ResponseEntity<List<DailyAccounting>> getAllTutorials(@RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            List<DailyAccounting> tutorials = dailyAccountingRepository.findAll(Sort.by(orders));

            if (tutorials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(tutorials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dailyAccounting")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllTutorialsPage(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,desc") String[] sort) {

        try {
            List<Order> orders = new ArrayList<Order>();

            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
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
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dailyAccounting/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<DailyAccounting> getTutorialById(@PathVariable("id") long id) {
        Optional<DailyAccounting> dailyAccountingData = dailyAccountingRepository.findById(id);

        if (dailyAccountingData.isPresent()) {
            return new ResponseEntity<>(dailyAccountingData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/dailyAccounting")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> createTutorial(@RequestBody DailyAccountingRequest dailyAccountingRequest) {

        dailyAccountingService.createDailyAccounting(dailyAccountingRequest);

        return ResponseEntity.accepted().build();

    }

    @PutMapping("/dailyAccounting/{id}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<DailyAccounting> updateTutorial(@PathVariable("id") long id, @RequestBody DailyAccounting tutorial) {
        Optional<DailyAccounting> tutorialData = dailyAccountingRepository.findById(id);

        if (tutorialData.isPresent()) {
            DailyAccounting _tutorial = tutorialData.get();

            _tutorial.setTitle(tutorial.getTitle());

            _tutorial.setRecipeToday(tutorial.getRecipeToday());
            _tutorial.setBalancePreviousMonth(tutorial.getBalancePreviousMonth());

            _tutorial.setOperationTreasuryAnterior(tutorial.getOperationTreasuryAnterior());
            _tutorial.setOperationTreasuryToday(tutorial.getOperationTreasuryToday());

            _tutorial.setOperationPreviousRegulation(tutorial.getOperationPreviousRegulation());
            _tutorial.setOperationRegulationToday(tutorial.getOperationRegulationToday());

            _tutorial.setPostCurrentAccount(tutorial.getPostCurrentAccount());
            _tutorial.setCreditExpected(tutorial.getCreditExpected());

            _tutorial.setRateExpected(tutorial.getRateExpected());

            _tutorial.setOtherValues(tutorial.getOtherValues());
            _tutorial.setStatesRepartition(tutorial.getStatesRepartition());
            _tutorial.setMoneySpecies(tutorial.getMoneySpecies());

            return new ResponseEntity<>(dailyAccountingRepository.save(_tutorial), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/dailyAccounting/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
        try {
            dailyAccountingRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/dailyAccounting")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllTutorials() {
        try {
            dailyAccountingRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
