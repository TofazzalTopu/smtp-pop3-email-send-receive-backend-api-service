package com.email.emailservice.model;

import com.email.emailservice.service.dtos.FetchProtocolCredential;
import com.email.emailservice.service.dtos.SmtpCredential;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document(collection = "connected_email_accounts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class ConnectedEmailAccount {

    @Id
    private String id;

    @NotNull
    private Long companyId;

    @NotNull
    private Long userId;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private boolean active;

    @NotNull
    private boolean verified;

    @NotNull
    private boolean enabled;

    @NotNull
    private boolean fetchProtocolEnabled;

    @NotNull
    private boolean fetchProtocolConnected;

    @NotBlank
    private String fetchProtocol;

    private FetchProtocolCredential fetchProtocolCredential;

    @NotNull
    private boolean smtpEnabled;

    @NotNull
    private boolean smtpConnected;

    private SmtpCredential smtpCredential;

    @NotNull
    private Date created;

    private Date updated;

    private String verificationToken;

}
