package com.healoui.DailyAccountingServerSide.payload.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String uniqueIdentifier;
    private String cin;
    private String recipeName;
    private String recipeCode;

}
