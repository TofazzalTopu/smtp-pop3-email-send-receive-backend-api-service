package com.email.emailservice.repository;

import com.email.emailservice.model.MailContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MailContentRepository extends MongoRepository<MailContent, String> {

    boolean existsBySenderEmailAndCompanyIdAndUserId(String senderEmail, Long companyId, Long userId);
    List<MailContent> findByDraft(boolean draft);
    Optional<MailContent> findByIdAndCompanyIdAndUserId(String id, Long companyId, Long userId);
    List<MailContent> findByCompanyIdAndUserIdAndLabelIsGreaterThan(Long companyId, Long userId, long label);
}
