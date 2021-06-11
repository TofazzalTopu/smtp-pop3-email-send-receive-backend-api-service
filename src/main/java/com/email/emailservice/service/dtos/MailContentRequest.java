package com.email.emailservice.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

/**
 * author: Tofazzal
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MailContentRequest {

    private String parentId;

    @NotBlank
    private String senderEmail;

    @NotBlank
    private String senderName;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;

    private String flags;

    private int messageNumber;

    private List<String> replyTo;

    @NotEmpty
    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    private String description;

    private String disposition;

    private int lineCount;

    private boolean draft;

    private boolean reply;

    private boolean forward;

    private boolean sent;

    @NotEmpty
    private Set<String> labels;

}
