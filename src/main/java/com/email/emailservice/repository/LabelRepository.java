package com.email.emailservice.repository;

import com.email.emailservice.model.Label;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends MongoRepository<Label, String> {
    Optional<Label> findByCompanyIdAndUserIdAndName(Long companyId, Long userId, String labelName);
    Optional<Label> findByIdAndCompanyIdAndUserId(String id, Long companyId, Long userId);
    List<Label> findByCompanyIdAndUserId(Long companyId, Long userId);
}
