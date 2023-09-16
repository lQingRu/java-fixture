package com.lqr.javafixture;

import com.lqr.javafixture.domain.Person;
import com.lqr.javafixture.domain.fixturemonkey.model.FixtureMonkeyPersonModelFactory;
import org.junit.jupiter.api.Test;

public class FixtureMonkeyPersonTest {

    @Test
    public void testGenerateBasePerson() {
        Person person = FixtureMonkeyPersonModelFactory.getFixtureMonkeyPerson();
        System.out.println(person.toString());
    }

    @Test
    public void testGenerateCustomPerson() {
        Person person = FixtureMonkeyPersonModelFactory.getFixtureMonkeyCustomPerson();
        System.out.println(person);
    }
}
