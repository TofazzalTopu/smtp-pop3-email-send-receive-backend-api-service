package com.email.emailservice.service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@NoArgsConstructor
public class MailUpdateRequest {
    @NotEmpty
    String mailId;
    @NotEmpty
    Set<String> labelNames;
}
