package com.dreamgames.backendengineeringcasestudy.domain.request;

import com.dreamgames.backendengineeringcasestudy.domain.enums.Country;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record UserSaveRequest (
        @NotBlank String username
) {
}