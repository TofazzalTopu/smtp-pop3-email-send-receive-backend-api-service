package com.email.emailservice.service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SmtpCredential {

    @NotBlank
    private String server;

    @NotBlank
    private String port;

    @NotNull
    private boolean requireSecurity = false;

    @NotBlank
    private String securityProtocol;

    @NotNull
    private boolean requireAuthentication = false;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
