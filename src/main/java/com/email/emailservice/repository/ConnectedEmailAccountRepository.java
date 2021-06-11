package com.email.emailservice.repository;

import com.email.emailservice.model.ConnectedEmailAccount;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConnectedEmailAccountRepository extends MongoRepository<ConnectedEmailAccount, String> {
    List<ConnectedEmailAccount> findByCompanyIdAndUserId(Long companyId, Long userId);

    Optional<ConnectedEmailAccount> findByIdAndCompanyIdAndUserId(String id, Long companyId, Long userId);

    Optional<ConnectedEmailAccount> findByEmailAndCompanyIdAndUserId(String email, Long companyId, Long userId);

    Optional<ConnectedEmailAccount> findFirstByEmailAndCompanyId(String email, Long companyId);

    Optional<ConnectedEmailAccount> findByVerificationToken(UUID token);
}
