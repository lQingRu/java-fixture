package com.lqr.javafixture.domain.fixturemonkey.model;

import com.lqr.javafixture.domain.Person;
import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.validator.DefaultArbitraryValidator;
import com.navercorp.fixturemonkey.javax.validation.plugin.JavaxValidationPlugin;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Property;
import net.jqwik.api.arbitraries.ListArbitrary;
import net.jqwik.web.api.EmailArbitrary;
import net.jqwik.web.api.Web;

import java.util.List;

public class FixtureMonkeyPersonModelFactory {

    static List<String> countries = List.of("Singapore", "Malaysia", "Indonesia");
    static List<String> gender = List.of("F", "M");

    public static Person getFixtureMonkeyPerson() {
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder().plugin(new JavaxValidationPlugin())
                //                .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
                .build();
        return fixtureMonkey.giveMeOne(Person.class);
    }

    @Property(seed = "2")
    public static Person getFixtureMonkeyCustomPerson() {
        FixtureMonkey fixtureMonkey =
                FixtureMonkey.builder().arbitraryValidator(new DefaultArbitraryValidator())
                        .plugin(new JavaxValidationPlugin()).build();

        // Create unique list of allowable values
        ListArbitrary<String> countryArbitrary = Arbitraries.of(countries).list().uniqueElements();
        // Create element from list of allowable values (somehow there are issues with @Pattern)
        Arbitrary<String> genderArbitrary = Arbitraries.of(gender);

        EmailArbitrary emailArbitrary = Web.emails();
        ArbitraryBuilder<Person> personArbitraryBuilder = fixtureMonkey.giveMeBuilder(Person.class)
                .set("gender", Arbitraries.of(genderArbitrary)).set("countries", countryArbitrary)
                .set("name", Arbitraries.strings().ofMaxLength(20)).set("email", emailArbitrary)
                .set("hobbies", countryArbitrary);
        Arbitrary<Person> personArbitrary = personArbitraryBuilder.build();
        return personArbitrary.sample();
    }



}
