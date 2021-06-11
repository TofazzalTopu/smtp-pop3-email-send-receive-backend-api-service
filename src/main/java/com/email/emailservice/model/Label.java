package com.email.emailservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "labels")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Label {
    @Id
    private String id;

    private String name;

    private long labelValue;

    @NotNull
    private Long companyId;
    @NotNull
    private Long userId;

}
