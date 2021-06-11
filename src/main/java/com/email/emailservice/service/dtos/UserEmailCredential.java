package com.email.emailservice.service.dtos;

import com.email.emailservice.enums.EmailProtocal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailCredential {
    private EmailProtocal protocol;
    private String server;
    private String port;
    private String username;
    private String password;
}
