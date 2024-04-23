package com.crud.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhoneDTO {

    @NotBlank
    private String number;

    @NotBlank
    private String cityCode;

    @NotBlank
    private String countryCode;
}
