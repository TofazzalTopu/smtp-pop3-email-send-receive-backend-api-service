package com.email.emailservice.service;

import com.email.emailservice.model.Label;
import com.email.emailservice.service.dtos.LabelUpdateRequest;

import java.util.List;
import java.util.Set;

public interface LabelService {
    Label save(Long companyId, Long userId, String name) throws Exception;

    Label update(Long companyId, Long userId, LabelUpdateRequest request) throws Exception;

    List<Label> findAll(Long companyId, Long userId);

    Long findByLabelName(Long companyId, Long userId, String labelName) throws Exception;

    Set<String> getLabelNamesByValue(Long companyId, Long userId, Long labelValue);

    Long getLabelValueByName(Long companyId, Long userId, Set<String> labelNames);

    void delete(Long companyId, Long userId, String id);

}
