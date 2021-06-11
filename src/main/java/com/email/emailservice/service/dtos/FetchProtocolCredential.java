package com.email.emailservice.service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class FetchProtocolCredential {
    @NotBlank
    private String server;

    @NotBlank
    private String port;

    private boolean requireSecurity = true;

    @NotBlank
    private String securityProtocol;

    private boolean requireAuthentication = true;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
