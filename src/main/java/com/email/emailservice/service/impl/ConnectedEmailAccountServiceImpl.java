package com.email.emailservice.service.impl;

import com.email.emailservice.config.SystemEmailService;
import com.email.emailservice.config.UserEmailServerConfigurationProvider;
import com.email.emailservice.config.providerConfig.EmailSenderServerConfigurationProvider;
import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.NotFoundException;
import com.email.emailservice.model.ConnectedEmailAccount;
import com.email.emailservice.repository.ConnectedEmailAccountRepository;
import com.email.emailservice.service.ConnectedEmailAccountService;
import com.email.emailservice.service.MailListService;
import com.email.emailservice.service.dtos.ConnectedEmailAccountDto;
import com.email.emailservice.service.dtos.SystemEmailInformation;
import com.email.emailservice.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import javax.mail.SendFailedException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConnectedEmailAccountServiceImpl implements ConnectedEmailAccountService {

    private final ConnectedEmailAccountRepository connectedEmailAccountRepository;
    private final MailListService mailListService;
    private final EmailSenderServerConfigurationProvider appConfig;
    private final SystemEmailService systemEmailSender;

    public ConnectedEmailAccountServiceImpl(EmailSenderServerConfigurationProvider appConfig,
                                            ConnectedEmailAccountRepository connectedEmailAccountRepository,
                                            MailListService mailListService, SystemEmailService systemEmailSender) {
        this.appConfig = appConfig;
        this.connectedEmailAccountRepository = connectedEmailAccountRepository;
        this.mailListService = mailListService;
        this.systemEmailSender = systemEmailSender;
    }

    @Override
    public ConnectedEmailAccountDto save(ConnectedEmailAccountDto emailAccountDto, Long companyId, Long userId)
            throws Exception {

        ConnectedEmailAccount account = connectedEmailAccountRepository
                .findByEmailAndCompanyIdAndUserId(emailAccountDto.getEmail(), companyId, userId)
                .orElseGet(ConnectedEmailAccount::new);

        String verificationToken = UUID.randomUUID().toString();
        account.setUserId(userId);
        account.setCompanyId(companyId);
        account.setEmail(emailAccountDto.getEmail());
        account.setActive(true);
        account.setVerified(false);
        account.setFetchProtocolConnected(false);
        account.setFetchProtocolEnabled(false);
        account.setFetchProtocol("POP3");
        account.setFetchProtocolCredential(null);
        account.setSmtpEnabled(false);
        account.setSmtpConnected(false);
        account.setSmtpCredential(null);
        account.setVerificationToken(verificationToken);

        String urlEndpoint = "/api/" + AppConstant.API_VERSION + "/companies/" + companyId + "/users/" + userId
                + "/email-accounts/verify?token=" + verificationToken;
        systemEmailSender.setTemplateName("account-verfication-email.html");
        SystemEmailInformation<String> emailContent = SystemEmailInformation.<String>builder().companyId(companyId)
                .email(account.getEmail()).userId(userId)
                .content(AppConstant.VERIFICATION_EMAIL_CONTENT.replace("{{URL}}", urlEndpoint)).build();
        if (!systemEmailSender.send(emailContent)) {
            throw new SendFailedException(AppConstant.EMAIL_ACCOUNTS_EMAIL_SEND_FAILED + appConfig.getSenderUsername());
        }
        BeanUtils.copyProperties(account, emailAccountDto);
        connectedEmailAccountRepository.save(account);
        return emailAccountDto;
    }

    @Override
    public List<ConnectedEmailAccountDto> findAll(Long companyId, Long userId) {
        return connectedEmailAccountRepository.findByCompanyIdAndUserId(companyId, userId).stream().map(account -> {
            var accountDto = new ConnectedEmailAccountDto();
            BeanUtils.copyProperties(account, accountDto);
            return accountDto;
        }).collect(Collectors.toList());

    }

    @Override
    public ConnectedEmailAccountDto update(ConnectedEmailAccountDto emailAccountDto, String id, Long companyId,
                                           Long userId) throws Exception {
        ConnectedEmailAccount account = connectedEmailAccountRepository
                .findByIdAndCompanyIdAndUserId(id, companyId, userId)
                .orElseThrow(() -> new NotFoundException(AppConstant.EMAIL_ACCOUNTS_NOT_FOUND));

        if (!account.isVerified()) {
            throw Problem.builder().withStatus(Status.NOT_ACCEPTABLE).withTitle(Status.NOT_ACCEPTABLE.name())
                    .withDetail(AppConstant.EMAIL_ACCOUNTS_NOT_VERIFIED).build();
        }
        if (account.isSmtpConnected() && account.isFetchProtocolConnected()) {
            throw Problem.builder().withStatus(Status.NOT_ACCEPTABLE).withTitle(Status.NOT_ACCEPTABLE.name())
                    .withDetail(AppConstant.EMAIL_ACCOUNTS_ALREADY_CONNECTED).build();
        }

        BeanUtils.copyProperties(emailAccountDto, account);
        UserEmailServerConfigurationProvider provider =
                new UserEmailServerConfigurationProvider(account.getSmtpCredential(), account.getFetchProtocolCredential());

        if (account.isSmtpEnabled() && provider.isFetchServerConnected()) {
            String encryptedSmtpPassword = SecurityUtils.encryptPassword(account.getSmtpCredential().getPassword());
            account.setSmtpConnected(true);
            account.setSmtpEnabled(true);
            account.getSmtpCredential().setPassword(encryptedSmtpPassword);
            account.getSmtpCredential().setRequireSecurity(true);
            account.getSmtpCredential().setRequireAuthentication(true);
        } else {
            account.setSmtpCredential(null);
        }

        if (account.isFetchProtocolEnabled() && provider.isFetchServerConnected()) {
            String encryptedFetchPassword = SecurityUtils.encryptPassword(account.getFetchProtocolCredential().getPassword());
            account.setFetchProtocolConnected(true);
            account.setFetchProtocolEnabled(true);
            account.getFetchProtocolCredential().setPassword(encryptedFetchPassword);
            account.getFetchProtocolCredential().setRequireSecurity(true);
            account.getFetchProtocolCredential().setRequireAuthentication(true);
        } else {
            account.setFetchProtocolCredential(null);
        }

        account.setUpdated(new Date());
        account = connectedEmailAccountRepository.save(account);
        account.getFetchProtocolCredential().setPassword(null);
        account.getSmtpCredential().setPassword(null);
        BeanUtils.copyProperties(account, emailAccountDto);
        return emailAccountDto;
    }

    @Override
    public ConnectedEmailAccountDto findByIdForCompanyAndUserId(String id, Long companyId, Long userId) {
        var account = connectedEmailAccountRepository.findByIdAndCompanyIdAndUserId(id, companyId, userId)
                .orElseThrow(() -> new NotFoundException(AppConstant.EMAIL_ACCOUNTS_NOT_FOUND));
        var accountDto = new ConnectedEmailAccountDto();
        BeanUtils.copyProperties(account, accountDto);
        return accountDto;
    }

    @Override
    public void delete(String id, Long companyId, Long userId) {
        ConnectedEmailAccount account = connectedEmailAccountRepository.findByIdAndCompanyIdAndUserId(id, companyId, userId)
                .orElseThrow(() -> new NotFoundException(AppConstant.EMAIL_ACCOUNTS_NOT_FOUND));

        if (mailListService.isEmailExistByEmailCompanyIdAndUserId(account.getEmail(), companyId, userId)) {
            throw Problem.builder().withStatus(Status.NOT_ACCEPTABLE).withTitle(Status.NOT_ACCEPTABLE.name())
                    .withDetail(AppConstant.EMAIL_ACCOUNTS_ALREADY_CONNECTED).build();
        }
        account.setActive(false);
        connectedEmailAccountRepository.save(account);
    }

    @Override
    public void verify(String token) {
        ConnectedEmailAccount account = connectedEmailAccountRepository.findByVerificationToken(UUID.fromString(token))
                .orElseThrow(() -> new NotFoundException(AppConstant.EMAIL_ACCOUNTS_NOT_FOUND));

        account.setVerified(true);
        account.setEnabled(true);
        account.setVerificationToken(null);
        connectedEmailAccountRepository.save(account);
    }

    @Override
    public boolean isEmailExists(String email, Long companyId) {
        return connectedEmailAccountRepository.findFirstByEmailAndCompanyId(email, companyId).isPresent();
    }
}
