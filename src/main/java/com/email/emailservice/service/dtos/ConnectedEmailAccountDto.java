package com.email.emailservice.service.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ConnectedEmailAccountDto {

    @NotNull
    private Long companyId;

    @NotNull
    private Long userId;

    @NotBlank
    @Email
    private String email;

    private boolean active = true;

    private boolean verified = false;

    private boolean fetchProtocolEnabled = false;

    private boolean fetchProtocolConnected = false;

    @NotBlank
    private String fetchProtocol;

    private FetchProtocolCredential fetchProtocolCredential;

    private boolean smtpEnabled = false;

    private boolean smtpConnected = false;

    private SmtpCredential smtpCredential;

    private boolean verificationForcedByUser = false;


}
