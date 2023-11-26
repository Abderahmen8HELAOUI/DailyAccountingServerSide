package com.healoui.DailyAccountingServerSide.security.sevices;

import com.healoui.DailyAccountingServerSide.models.DailyAccounting;
import com.healoui.DailyAccountingServerSide.payload.request.DailyAccountingRequest;
import com.healoui.DailyAccountingServerSide.repository.DailyAccountingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DailyAccountingService {

    private final DailyAccountingRepository dailyAccountingRepository;

    public void createDailyAccounting(DailyAccountingRequest dailyAccountingRequest){
        LocalDateTime currentLocalDateTime = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        String formattedDateTime = currentLocalDateTime.format(dateTimeFormatter);

        DailyAccounting dailyAccounting = DailyAccounting.builder()
                .title("OperationDate is : " + formattedDateTime)
                .recipeToday(dailyAccountingRequest.getRecipeToday())
                .balancePreviousMonth(dailyAccountingRequest.getBalancePreviousMonth())
                .operationTreasuryAnterior(dailyAccountingRequest.getOperationTreasuryAnterior())
                .operationTreasuryToday(dailyAccountingRequest.getOperationTreasuryToday())
                .operationPreviousRegulation(dailyAccountingRequest.getOperationPreviousRegulation())
                .operationRegulationToday(dailyAccountingRequest.getOperationRegulationToday())
                .postCurrentAccount(dailyAccountingRequest.getPostCurrentAccount())
                .creditExpected(dailyAccountingRequest.getCreditExpected())
                .rateExpected(dailyAccountingRequest.getRateExpected())
                .otherValues(dailyAccountingRequest.getOtherValues())
                .statesRepartition(dailyAccountingRequest.getStatesRepartition())
                .moneySpecies(dailyAccountingRequest.getMoneySpecies())
                .build();
        dailyAccountingRepository.save(dailyAccounting);

    }
}
