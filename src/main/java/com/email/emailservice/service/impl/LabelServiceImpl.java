package com.email.emailservice.service.impl;

import com.email.emailservice.constant.AppConstant;
import com.email.emailservice.exceptions.NotFoundException;
import com.email.emailservice.model.Label;
import com.email.emailservice.repository.LabelRepository;
import com.email.emailservice.service.LabelService;
import com.email.emailservice.service.dtos.LabelUpdateRequest;
import org.jsoup.internal.StringUtil;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public Label save(Long companyId, Long userId, String name) {
        Label label = new Label();
        if (labelRepository.findByCompanyIdAndUserIdAndName(companyId, userId, name.strip().toUpperCase()).isPresent()) {
            throw Problem.builder().withTitle(Status.NOT_ACCEPTABLE.name()).withStatus(Status.NOT_ACCEPTABLE)
                    .withDetail(name.toUpperCase() + ": " + AppConstant.LABEL_ALREADY_EXIST).build();
        }
        long size = labelRepository.findByCompanyIdAndUserId(companyId, userId).size() + 1;
        label.setLabelValue((long) Math.pow(2, size));
        label.setName(name.toUpperCase());
        label.setCompanyId(companyId);
        label.setUserId(userId);
        return labelRepository.save(label);
    }

    @Override
    public Label update(Long companyId, Long userId, LabelUpdateRequest request) {
        Label label = labelRepository.findByIdAndCompanyIdAndUserId(request.getLabelId(), companyId, userId)
                .orElseThrow(() -> new NotFoundException(request.getLabelName().toUpperCase() + ": " + AppConstant.LABEL_NOT_FOUND));

        labelRepository.findByCompanyIdAndUserIdAndName(companyId, userId, request.getLabelName().strip().toUpperCase())
                .ifPresent(lbl -> {
                    if (!Objects.equals(lbl.getId(), label.getId())) {
                        throw Problem.builder().withTitle(Status.NOT_ACCEPTABLE.name()).withStatus(Status.NOT_ACCEPTABLE)
                                .withDetail(request.getLabelName().toUpperCase() + ": " + AppConstant.LABEL_ALREADY_EXIST).build();
                    }
                });

        label.setName(request.getLabelName().toUpperCase());
        return labelRepository.save(label);
    }

    @Override
    public List<Label> findAll(Long companyId, Long userId) {
        return labelRepository.findByCompanyIdAndUserId(companyId, userId);
    }

    @Override
    public Long findByLabelName(Long companyId, Long userId, String labelName) throws NotFoundException {
        return labelRepository.findByCompanyIdAndUserIdAndName(companyId, userId, labelName.strip().toUpperCase())
                .map(Label::getLabelValue).orElseThrow(() -> new NotFoundException(labelName.toUpperCase() + ": " + AppConstant.LABEL_NOT_FOUND));
    }

    @Override
    public Set<String> getLabelNamesByValue(Long companyId, Long userId, Long labelValue) {
        return labelRepository.findByCompanyIdAndUserId(companyId, userId).stream()
                .filter(label -> (labelValue & label.getLabelValue()) == label.getLabelValue()).map(Label::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public Long getLabelValueByName(Long companyId, Long userId, Set<String> labelNames) {
        List<Long> values = labelRepository.findByCompanyIdAndUserId(companyId, userId).stream()
                .filter(label -> labelNames.contains(label.getName().strip().toUpperCase())).map(Label::getLabelValue)
                .collect(Collectors.toList());

        if (labelNames.size() != values.size()) {
            throw new NotFoundException(
                    AppConstant.ONE_OR_MORE_LABEL_NOT_FOUND + StringUtil.join(labelNames, ","));
        }

        return values.stream().reduce((labelVal1, labelVal2) -> labelVal1 | labelVal2).get();
    }

    @Override
    public void delete(Long companyId, Long userId, String id) {
        Label label = labelRepository.findByIdAndCompanyIdAndUserId(id, companyId, userId)
                .orElseThrow(() -> new NotFoundException(AppConstant.LABEL_NOT_FOUND));
        labelRepository.delete(label);
    }
}
