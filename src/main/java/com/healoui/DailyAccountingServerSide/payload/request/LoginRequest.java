package com.healoui.DailyAccountingServerSide.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String uniqueIdentifier;

    @NotBlank
    private String cin;

    @NotBlank
    private String recipeName;

    @NotBlank
    private String recipeCode;
}
