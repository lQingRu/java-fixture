package com.lqr.javafixture.domain.fixturemonkey.model;

import com.lqr.javafixture.domain.fixturemonkey.CommsInfo;
import com.lqr.javafixture.domain.fixturemonkey.InternetSubscriptionAccount;
import com.lqr.javafixture.domain.fixturemonkey.OnlineAccount;
import com.navercorp.fixturemonkey.FixtureMonkey;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

public class FixtureMonkeyCommsInfoModelFactory {

    public static CommsInfo getFixtureMonkeyCommsInfo() {
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder().build();
        Arbitrary<OnlineAccount> onlineAccountArbitrary =
                fixtureMonkey.giveMeBuilder(OnlineAccount.class).build();
        Arbitrary<InternetSubscriptionAccount> internetSubscriptionArbitrary =
                fixtureMonkey.giveMeBuilder(InternetSubscriptionAccount.class).build();

        Arbitrary<CommsInfo> commsInfoArbitrary =
                Arbitraries.oneOf(onlineAccountArbitrary, internetSubscriptionArbitrary);
        return commsInfoArbitrary.sample();
    }
}
