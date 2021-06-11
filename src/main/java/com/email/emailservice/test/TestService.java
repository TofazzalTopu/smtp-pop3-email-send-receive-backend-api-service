package com.email.emailservice.test;

import com.email.emailservice.model.MailContent;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TestService {
    MailContent sendMail(List<MultipartFile> files) throws Exception;

    default MailContent endMail(List<MultipartFile> files) {
        return null;
    }

    final int x=8;
    int y=8;
    static int z=8;

}
