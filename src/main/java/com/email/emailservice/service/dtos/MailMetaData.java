package com.email.emailservice.service.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.mail.Address;
import javax.mail.Flags;
import java.util.List;

@Data
@NoArgsConstructor
public class MailMetaData {
    private List<Address> senderEmail;
    private List<Address> to;
    private List<Address> cc;
    private List<Address> bcc;
    private List<Address> replyTo;
    private List<String> fileNames;
    private Flags flags;
    private String attachmentPath;
    private int lineCount;
    private int messageNumber;
    private String description;
    private String disposition;
}
