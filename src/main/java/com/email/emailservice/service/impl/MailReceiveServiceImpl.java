package com.email.emailservice.service.impl;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.model.MailContent;
import com.email.emailservice.repository.MailContentRepository;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.MailReceiveService;
import com.email.emailservice.service.connection.MailConnectionService;
import com.email.emailservice.service.dtos.MailContentStore;
import com.email.emailservice.service.dtos.MailMetaData;
import com.email.emailservice.utils.AppUtils;
import com.email.emailservice.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MailReceiveServiceImpl implements MailReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailReceiveServiceImpl.class);

    @Value("${itc.email-service.fetch.host}")
    public String HOST;

    @Value("${itc.email-service.fetch.username}")
    public String USERNAME;

    @Value("${itc.email-service.fetch.password}")
    public String PASSWORD;

    @Value("${itc.email-service.fetch.protocol}")
    public String STORETYPE;

    @Value("${itc.email-service.file.attachment.path}")
    private String FILE_ATTACHMENT_PATH;

    @Value("${itc.email-service.label.inbox}")
    private String LABEL_INBOX;

    private final String INBOX = "INBOX";

    private final MailContentRepository mailContentRepository;
    private final MailConnectionService mailConnectionService;
    private final LabelService labelService;

    public MailReceiveServiceImpl(MailConnectionService mailConnectionService,
            MailContentRepository mailContentRepository, LabelService labelService) {
        this.mailConnectionService = mailConnectionService;
        this.mailContentRepository = mailContentRepository;
        this.labelService = labelService;
    }

    @Override
    public List<MailContent> receiveMailList(Long companyId, Long userId) throws Exception {
        Session session = mailConnectionService.connectToPOP3MailServer();
        if (session == null) {
            throw new AuthenticationFailedException(AppConstant.INCORRECT_USER_NAME_PASSWORD);
        }
        // 4. Get the POP3 store provider and connect to the store.
        Store store = session.getStore(STORETYPE);
        store.connect(HOST, USERNAME, PASSWORD);

        // 5. Get folder and open the INBOX folder in the store.
        Folder inbox = store.getFolder(INBOX);
        inbox.open(Folder.READ_ONLY);

        // 6. Retrieve the messages from the folder.
        Message[] messages = inbox.getMessages();
        LOGGER.info(AppConstant.MAIL_RECEIVED_SUCCESS);

        // 7. Close folder and close store.
        saveMailContentMetaData(messages, companyId, userId);
        Long labelValue = labelService.getLabelValueByName(companyId, userId, Set.of(LABEL_INBOX));
        List<MailContent> mailContentList = mailContentRepository
                .findByCompanyIdAndUserIdAndLabelIsGreaterThan(companyId, userId, 0L)
                .stream().filter(mail-> (mail.getLabel() & labelValue)== labelValue)
                .collect(Collectors.toList());
        mailContentList.stream().forEach(m -> m.setLabels(labelService.getLabelNamesByValue(companyId, userId, m.getLabel())));
        inbox.close(false);
        store.close();
        return mailContentList;
    }

    // save mail content meta data which was fetched from server.
    private List<MailContent> saveMailContentMetaData(Message[] messages, Long companyId, Long userId)
            throws Exception {
        List<MailContent> mailContentList = new ArrayList<>();
        List<BodyPart> bodyPartList = new ArrayList<>();
        for (Message message : messages) {
            MailContent mailContent = new MailContent();
            String senderEmail = message.getFrom()[0].toString();
            if (senderEmail.contains("<")) {
                String[] from = message.getFrom()[0].toString().split("<");
                String[] email = from[1].split(">");
                mailContent.setSenderEmail(email[0]);
                mailContent.setSenderName(from[0]);
            } else {
                mailContent.setSenderEmail(senderEmail);
            }
            mailContent.setSubject(message.getSubject());
            mailContent.setReceivedDate(message.getReceivedDate());
            mailContent.setSentDate(message.getSentDate());
            mailContent.setCompanyId(companyId);
            mailContent.setUserId(userId);
            mailContent.setLabel(labelService.getLabelValueByName(companyId, userId, Set.of(LABEL_INBOX)));

            MailContentStore mailContentStore = AppUtils.getMailContent(message);
            mailContent.setContent(mailContentStore.getBodyText());
            bodyPartList.addAll(mailContentStore.getBodyPartList());

            mailContent.setMessageObject(buildMailMetaData(companyId, userId, message, mailContentStore));
            mailContentList.add(mailContent);
        }
        FileUtils.saveFiles(bodyPartList,
                FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        mailContentRepository.saveAll(mailContentList);

        LOGGER.info(AppConstant.MAIL_SAVE_SUCCESS);
        return mailContentList;
    }

    private MailMetaData buildMailMetaData(Long companyId, Long userId, Message message, MailContentStore mailContentStore) throws MessagingException {
        MailMetaData mailMetaData = new MailMetaData();
        mailMetaData.setSenderEmail(Arrays.asList(message.getFrom()));
        mailMetaData.setTo(Arrays.asList(message.getRecipients(Message.RecipientType.TO)));
        mailMetaData.setCc(Arrays.asList(message.getRecipients(Message.RecipientType.CC) != null ? message.getRecipients(Message.RecipientType.CC) : new Address[0]));
        mailMetaData.setBcc(Arrays.asList(message.getRecipients(Message.RecipientType.BCC) != null ? message.getRecipients(Message.RecipientType.BCC) : new Address[0]));
        mailMetaData.setReplyTo(Arrays.asList(message.getReplyTo()));
        mailMetaData.setFileNames(mailContentStore.getFileNameList());
        mailMetaData.setFlags(message.getFlags());
        mailMetaData.setAttachmentPath(FILE_ATTACHMENT_PATH + File.separator + companyId + File.separator + userId);
        mailMetaData.setLineCount(message.getLineCount());
        mailMetaData.setMessageNumber(message.getMessageNumber());
        mailMetaData.setDescription(message.getDescription());
        mailMetaData.setDisposition(message.getDisposition());
        return mailMetaData;
    }
}
