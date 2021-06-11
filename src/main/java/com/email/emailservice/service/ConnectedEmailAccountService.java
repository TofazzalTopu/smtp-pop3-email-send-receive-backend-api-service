package com.email.emailservice.service;

import com.email.emailservice.service.dtos.ConnectedEmailAccountDto;

import java.util.List;

public interface ConnectedEmailAccountService {

    ConnectedEmailAccountDto save(ConnectedEmailAccountDto emailAccountsDto, Long companyId, Long userId)
            throws Exception;

    List<ConnectedEmailAccountDto> findAll(Long companyId, Long userId);

    ConnectedEmailAccountDto update(ConnectedEmailAccountDto emailAccountsDto, String id, Long companyId, Long userId)
            throws Exception;

    void delete(String id, Long companyId, Long userId);

    void verify(String token) throws Exception;

    ConnectedEmailAccountDto findByIdForCompanyAndUserId(String id, Long companyId, Long userId);

    boolean isEmailExists(String email, Long companyId);

}
