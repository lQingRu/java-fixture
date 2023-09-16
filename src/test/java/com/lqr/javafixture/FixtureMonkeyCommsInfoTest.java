package com.lqr.javafixture;

import com.lqr.javafixture.domain.fixturemonkey.CommsInfo;
import com.lqr.javafixture.domain.fixturemonkey.model.FixtureMonkeyCommsInfoModelFactory;
import org.junit.jupiter.api.Test;

public class FixtureMonkeyCommsInfoTest {

    @Test
    public void testGenerateBaseCommsInfo() {
        CommsInfo commsInfo = FixtureMonkeyCommsInfoModelFactory.getFixtureMonkeyCommsInfo();
        System.out.println(commsInfo.toString());
    }

}
