package com.email.emailservice.service.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.mail.BodyPart;
import java.util.List;

@Data
@AllArgsConstructor
public class MailContentStore {
    private String bodyText;
    private List<String> fileNameList;
    private List<BodyPart> bodyPartList;
}
