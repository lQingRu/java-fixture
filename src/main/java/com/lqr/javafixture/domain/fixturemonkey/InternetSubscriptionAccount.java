package com.lqr.javafixture.domain.fixturemonkey;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class InternetSubscriptionAccount extends CommsInfo {

    private String internetProvider;
}
