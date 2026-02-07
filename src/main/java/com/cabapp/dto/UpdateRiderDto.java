package com.cabapp.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateRiderDto {
    // Only mutable fields
    private String name;

    @Pattern(regexp = "^\\d{10}$", message = "Phone must be 10 digits")
    private String phone;
}