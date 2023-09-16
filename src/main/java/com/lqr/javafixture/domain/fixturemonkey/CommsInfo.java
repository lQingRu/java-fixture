package com.lqr.javafixture.domain.fixturemonkey;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public abstract class CommsInfo {

    private String displayName;

    private CommsInfoType commsInfoType;

    private List<Usage> usages;

    private LocalDateTime lastUsedDate;

    private LocalDateTime purchasedDate;
}
