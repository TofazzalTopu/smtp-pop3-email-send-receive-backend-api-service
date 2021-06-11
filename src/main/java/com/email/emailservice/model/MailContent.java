package com.email.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Data
@Document(collection = "mail_content")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class MailContent {
    @Id
    private String id;
    private String parentId;
    @NotBlank
    private String senderEmail;
    private String senderName;
    @NotBlank
    private String subject;
    @NotBlank
    private String content;
    private Date receivedDate;
    private Date sentDate;
    private Object messageObject;
    private boolean draft;
    private boolean reply;
    private boolean forward;
    private boolean sent;
    @NotNull
    private Long label;
    @NotNull
    private Long companyId;
    @NotNull
    private Long userId;

    @Transient
    private Set<String> labels;


    @Override
    public String toString() {
        return "MailContent [id=" + id + ", senderEmail=" + senderEmail + ", senderName=" + senderName + ", subject=" + subject +
                ", content=" + content + ", receivedDate=" + receivedDate +
                "]";
    }
}
