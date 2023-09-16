package com.lqr.javafixture.domain.fixturemonkey;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class OnlineAccount extends CommsInfo {

    // populate only if domainType = ORGANIZATION
    private DomainType domainType;
    private String domainName;

}
