package com.healoui.DailyAccountingServerSide.payload.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 10,max = 10)
    private String uniqueIdentifier;

    @NotBlank
    @Size(min = 8,max = 8)
    private String cin;

    @NotBlank
    @Size(max = 120)
    private String recipeName;

    @NotBlank
    @Size(min = 3,max = 10)
    private String recipeCode;
}
