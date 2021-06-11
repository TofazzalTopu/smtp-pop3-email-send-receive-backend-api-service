package com.email.emailservice.service.impl;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.MailSendingService;
import com.email.emailservice.service.connection.MailConnectionService;
import com.email.emailservice.service.dtos.MailContentRequest;
import com.email.emailservice.service.dtos.MailMetaData;
import com.email.emailservice.utils.AppUtils;
import com.email.emailservice.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class MailSendingServiceImpl implements MailSendingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSendingServiceImpl.class);

    @Value("${itc.email-service.file.attachment.path}")
    private String FILE_ATTACHMENT_PATH;

    private final MailConnectionService mailConnectionService;
    private final MailContentRepository mailContentRepository;
    private final LabelService labelService;

    public MailSendingServiceImpl(MailConnectionService mailConnectionService,
            MailContentRepository mailContentRepository, LabelService labelService) {
        this.mailConnectionService = mailConnectionService;
        this.mailContentRepository = mailContentRepository;
        this.labelService = labelService;

    }

    // this send method is doing too much work. violates Single responsibility
    // principle.
    // Please refactor it @Tofazzal bhai.
    @Override
    public MailContent sendMail(Long companyId, Long userId, MailContentRequest request, List<MultipartFile> files)
            throws Exception {
        Session session = mailConnectionService.connectToSMTPMailServer();
        if (session == null) {
            throw new AuthenticationFailedException(AppConstant.INCORRECT_USER_NAME_PASSWORD);
        }
        FileUtils.saveFiles(files, FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        MailContent mailContent = buildMailContent(companyId, userId, request, files);
        mailContentRepository.save(mailContent);

        Transport transport = session.getTransport();
        MimeMessage message = buildMessage(companyId, userId, session, mailContent, files);
        transport.connect();
        Transport.send(message);
        transport.close();
        LOGGER.info(AppConstant.MAIL_SENT_SUCCESS);
        return mailContent;
    }

    private MimeMessage buildMessage(Long companyId, Long userId, Session session, MailContent mailContent, List<MultipartFile> files) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailContent.getSenderEmail()));
        message.setSubject(mailContent.getSubject());

        MailMetaData mailMetaData = (MailMetaData) mailContent.getMessageObject();
        message.addRecipients(Message.RecipientType.TO, mailMetaData.getTo().toArray(new Address[mailMetaData.getTo().size()]));
        message.addRecipients(Message.RecipientType.CC, mailMetaData.getCc().toArray(new Address[mailMetaData.getCc().size()]));
        message.addRecipients(Message.RecipientType.BCC, mailMetaData.getBcc().toArray(new Address[mailMetaData.getBcc().size()]));
        message.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mailContent.getContent(), "text/html");
        Multipart multipart = FileUtils.buildMultipart(files, FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        LOGGER.info(AppConstant.MAIL_MESSAGE_BUILD_SUCCESS);
        return message;
    }

    private MailContent buildMailContent(Long companyId, Long userId, MailContentRequest mailContentRequest,
            List<MultipartFile> files) throws Exception {
        MailContent mailContent = new MailContent();
        mailContent.setSenderEmail(mailContentRequest.getSenderEmail());
        mailContent.setSenderName(mailContentRequest.getSenderName());
        mailContent.setSubject(mailContentRequest.getSubject());
        mailContent.setContent(mailContentRequest.getContent());
        mailContent.setReceivedDate(new Date());
        mailContent.setSentDate(new Date());
        mailContent.setForward(mailContentRequest.isForward());
        mailContent.setDraft(mailContentRequest.isDraft());
        mailContent.setReply(mailContentRequest.isReply());
        mailContent.setSent(true);
        mailContent.setLabel(labelService.getLabelValueByName(companyId, userId, mailContentRequest.getLabels()));
        mailContent.setLabels(mailContentRequest.getLabels());
        mailContent.setCompanyId(companyId);
        mailContent.setUserId(userId);

        mailContent.setMessageObject(buildMailMetaData(companyId, userId, mailContentRequest, files));
        LOGGER.info(AppConstant.MAIL_CONTENT_BUILD_SUCCESS);
        return mailContent;
    }

    private MailMetaData buildMailMetaData(Long companyId, Long userId, MailContentRequest mailContentRequest, List<MultipartFile> files) throws Exception {
        MailMetaData mailMetaData = new MailMetaData();
        mailMetaData.setSenderEmail(Arrays.asList(AppUtils.buildInternetAddress(Arrays.asList(mailContentRequest.getSenderEmail()))));
        mailMetaData.setTo(Arrays.asList(AppUtils.buildInternetAddress(mailContentRequest.getTo())));
        mailMetaData.setCc(Arrays.asList(mailContentRequest.getCc() != null ? AppUtils.buildInternetAddress(mailContentRequest.getCc()) : new Address[0]));
        mailMetaData.setBcc(Arrays.asList(mailContentRequest.getBcc() != null ? AppUtils.buildInternetAddress(mailContentRequest.getBcc()) : new Address[0]));
        mailMetaData.setReplyTo(Arrays.asList(mailContentRequest.getReplyTo() != null ? AppUtils.buildInternetAddress(mailContentRequest.getReplyTo()) : new Address[0]));

        mailMetaData.setFileNames(FileUtils.getFileNames(files));
        mailMetaData.setAttachmentPath(FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        mailMetaData.setLineCount(mailContentRequest.getLineCount());
        mailMetaData.setMessageNumber(mailContentRequest.getMessageNumber());
        mailMetaData.setDescription(mailContentRequest.getDescription());
        mailMetaData.setDisposition(mailContentRequest.getDisposition());
        LOGGER.info(AppConstant.MAIL_META_DATA_BUILD_SUCCESS);
        return mailMetaData;
    }
}
