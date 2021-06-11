package com.email.emailservice.service.dtos;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SystemEmailInformation<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long companyId;
    private Long userId;
    private String email;
    private T content;
}
