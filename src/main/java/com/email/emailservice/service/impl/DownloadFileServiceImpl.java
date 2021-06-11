package com.email.emailservice.service.impl;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.NotFoundException;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.DownloadFileService;
import com.email.emailservice.service.dtos.MailMetaData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;

@Service
public class DownloadFileServiceImpl implements DownloadFileService {

    @Value("${itc.email-service.file.attachment.path}")
    private String FILE_ATTACHMENT_PATH;

    private final MailContentRepository mailContentRepository;

    public DownloadFileServiceImpl(MailContentRepository mailContentRepository) {
        this.mailContentRepository = mailContentRepository;

    }

    @Override
    public Resource download(Long companyId, Long userId, String id, String fileName) throws MalformedURLException {

        MailContent mailContent = mailContentRepository.findByIdAndCompanyIdAndUserId(id, companyId, userId).orElseThrow(() -> new NotFoundException(AppConstant.MAIL_NOT_FOUND));
        MailMetaData mailMetaData = (MailMetaData) mailContent.getMessageObject();
        return new UrlResource(Paths.get(FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId + File.separator + mailMetaData.getFileNames()
                .stream().filter(name -> name.equals(fileName)).findFirst().orElseThrow(()-> new NotFoundException(AppConstant.MAIL_ATTACHMENT_NOT_FOUND))).toUri());
    }
}
