package com.grepp.nbe563team04.app.controller.api.auth.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SigninRequest {

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 12)
    private String password;
}
