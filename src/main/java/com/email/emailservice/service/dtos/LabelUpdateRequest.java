package com.email.emailservice.service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class LabelUpdateRequest {
    @NotBlank
    String labelId;
    @NotBlank
    String labelName;
}
