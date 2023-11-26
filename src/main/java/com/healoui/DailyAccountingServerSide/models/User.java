package com.healoui.DailyAccountingServerSide.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @NotBlank
    @Size(max = 10)
    private String uniqueIdentifier;

    @NotBlank
    @Size(max = 8)
    private String cin;

    @NotBlank
    @Size(max = 120)
    private String recipeName;

    @NotBlank
    @Size(max = 10)
    private String recipeCode;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password, String uniqueIdentifier, String cin, String recipeName,String recipeCode) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.uniqueIdentifier = uniqueIdentifier;
        this.cin = cin;
        this.recipeName = recipeName;
        this.recipeCode = recipeCode;
    }
}
