# Fixture Monkey

## Dependencies

- Core

```xml

<dependency>
	<groupId>com.navercorp.fixturemonkey</groupId>
	<artifactId>fixture-monkey-starter</artifactId>
	<version>0.5.0</version>
</dependency>
```

- Bean Validations support

```xml

<dependency>
	<groupId>com.navercorp.fixturemonkey</groupId>
	<artifactId>fixture-monkey-javax-validation</artifactId>
	<version>0.5.0</version>
</dependency> 
```

## Pre-requisite (For Java)

### Immutable Type

**Method 1: Through `Constructor`**

- Requires each object to be mocked to be either:
    - A `record` class
    - Any constructors with `@ConstructorProperties`
        - If using `lombok`:
            - Can annotate class with `@Data` or `@ConstructorProperties`
            - Can also just add `lombok.anyConstructor.addConstructorProperties=true` in
              lombok.config

- Create Fixture Monkey with `objectIntrospector` option

```java
FixtureMonkey fixtureMonkey=FixtureMonkey.builder()
        .objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
        .build();
```

**Method 2: Through `Factory`**

- Requires each object to be mocked to have a static factory method
- Create Fixture Monkey with `objectIntrospector` option

```java
FixtureMonkey fixtureMonkey=FixtureMonkey.builder()
        .objectIntrospector(FactoryMethodArbitraryIntrospector.INSTANCE)
        .build();
```

**Method 3: Through `Jackson`**

- Requires each object to be mocked to be serializable/deserializable by Jackson
- Requires additional dependency: `com.navercorp.fixturemonkey:fixture-monkey-jackson`
- Create Fixture Monkey with `plugin` option

```java
FixtureMonkey fixtureMonkey=FixtureMonkey.builder()
        .plugin(new JacksonPlugin(objectMapper))
        .build();
```

### Mutable Type

**Method 1: Through `Constructor` (Same as the immutable type above)**

**Method 2: Through `FieldReflection`**

- Requires each object to be mocked to have:
    - No args constructor
    - Getter / Setter
- Create Fixture Monkey with `objectIntrospector` option

```java
FixtureMonkey fixtureMonkey=FixtureMonkey.builder()
        .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
        .build();
``` 

**Method 3: Through `Java Bean` (Default `objectIntrospector`)**

- Requires each object to be mocked to have:
    - No args constructor
    - Setter

## Useful features from Fixture Monkey

-

See [Fixture Monkey Component](https://naver.github.io/fixture-monkey/docs/v0.5/components/labmonkey/)
for the full list of features

### Validation

- Read more
  in [Fixture Monkey Arbitrary Validator](https://naver.github.io/fixture-monkey/docs/v0.5/components/arbitraryvalidator/)
- Invalid instance could not be generated
    - If an object is invalid which sampled over 10k times
      from `Arbitrary`, `TooManyFilterMissesException` will be thrown
    - See below in "@Pattern" bug section for a sample log
- By default, there is no validator included

**Bean Validation by Javax**

1. To enable bean validations by `Javax`, add `fixture-monkey-javax-validation` dependency
2. Add `plugin` option

```java
FixtureMonkey fixtureMonkey=FixtureMonkey.builder()
        .plugin(new JavaxValidationPlugin())
        .build();
```

**Bean Validation by Jakarta**

1. To enable bean validations by `Javax`, add `fixture-monkey-jakarta-validation` dependency
2. Add `plugin` option

```java
FixtureMonkey fixtureMonkey=FixtureMonkey.builder()
        .plugin(new JakartaValidationPlugin())
        .build();
```

**Arbitrary Validation**

- Current version only have `DefaultArbitraryValidator` which `javax.validation.Validator` would
  validate instance
- Can create own custom `Arbitrary Validator` by implementing `ArbitraryValidator`
- Can combine `Arbitrary Validators` with `CompositeFixtureValidator`
- Create `FixtureMonkey` with `arbitraryValidator` option

### Null instance

- Determines when a `null` instance is created with `defaultNullInjectGenerator` option

### InnerSpec

- This is an alternative from using `Arbitrary` to create custom logic for generation of mock data
- However, to date, there are no documentations on the usage of `InnerSpec`

## Jqwik - Arbitrary

- Arbitrary is a fixture in `jqwik`
    - It is used in fixture monkey to generate random data for the different types of data in the
      properties of Java object
- To use `Arbitrary` in Fixture Monkey:
    1. Create a Fixture Monkey generator for the object that we want to generate
    ```java
    FixtureMonkey fixtureMonkey = FixtureMonkey.builder().build();
    ```
    2. Create `Arbitrary` for whatever fields we want to have custom generation logic

    - E.g.:
  ```java
  Arbitrary<String> genderArbitrary = Arbitraries.of(gender);
   ```
    3. Use `ArbitraryBuilder` to set the fields using `Arbitrary` created
  ```java
    ArbitraryBuilder<Person> personArbitraryBuilder = fixtureMonkey.giveMeBuilder(Person.class)
                .set("gender", Arbitraries.of(genderArbitrary))
                ...
  ```
    4. Create the object
  ```java
   Person person = personArbitraryBuilder.build().sample();
  ```
- See [Arbitrary Component](https://naver.github.io/fixture-monkey/docs/v0.5/components/arbitrary/)
  for more details

### Other useful properties for `Arbitrary`

- Filtering
    - Include only part of the values generated by Arbitrary
    - `Arbitrary.filter(predicate)`
- Mapping
    - Start with existing arbitrary and use its generated values to build other objects from them
    - `Arbitrary.map(function)`

## Jqwik - Constraints

-

See [Jqwik Constraining Default Generation](https://jqwik.net/docs/current/user-guide.html#constraining-default-generation)
for more details

- Default parameter generation can be constrained with these additional annotations

### Commonly used

- Allow null values with probability
    - `@WithNull(value)`: Inject `null` with probability of value
        - Works for all generated types
- Set string length
    - `@StringLength(value, min, max)`: Set fixed length / range length
    - `@NotEmpty`
- Set string not blank
    - `@NotBlank`
- Characters
    - `@LowerChars`
    - `@NumericChars`
- Set Collection (List, Set, Stream, Iterator, Map, Array) size
    - `@Size(value, min, max)`: Set fixed length / range length
- Set uniqueness in container object (List, Set, Stream, Iterator, Array)
    - `@UniqueElements(by)`: Ensure elements are unique or unique in relation to a certain
      feature (`by`)

## Jqwik - Parameterized Type

- Jqwik can handle type variables and wildcard types as well
-

See [Handling of parameterized Types](https://jqwik.net/docs/current/user-guide.html#providing-variable-types)
for more details

## Jqwik - Custom meta-annotations

- Can also create own annotations apart from using Jqwik built-in annotations
- See [Self-Made Annotations](https://jqwik.net/docs/current/user-guide.html#self-made-annotations)
  for more details

## How-to

### Multi-valued Fields

- Create `ListArbitrary`
- For specific list of values, pass in through `Arbitraries.of(<list of values>)`

```java
List<String> countries=["Singapore","Malaysia",...];
        ListArbitrary<String> countryArbitrary=Arbitraries.of(countries).list(); 
```

- Useful properties:
    - `.uniqueElements()`: create unique elements in list
    - `.ofMaxSize(int maxSize)`, `.ofMinSize(int minSize)`
    - `.ofSize(int size)`: fix a size
    - See https://jqwik.net/docs/1.8.0/javadoc/net/jqwik/api/arbitraries/ListArbitrary.html

### Abstract classes with concrete subclasses

- Unable to simply just create superclass when its `abstract` type, i.e. cannot simply
  just `fixtureMonkey.giveMeOne(CommsInfo.class)` where `CommsInfo` is an abstract class
- One possible approach - creating minimal `Arbitrary` for each concrete class:

```java
Arbitrary<OnlineAccount> onlineAccountArbitrary=
        fixtureMonkey.giveMeBuilder(OnlineAccount.class).build();
        Arbitrary<InternetSubscriptionAccount> internetSubscriptionArbitrary=
        fixtureMonkey.giveMeBuilder(InternetSubscriptionAccount.class).build();

        Arbitrary<CommsInfo> commsInfoArbitrary=
        Arbitraries.oneOf(onlineAccountArbitrary,internetSubscriptionArbitrary);
```

### Nested fields

- Cannot simply do this to have custom logic for nested fields:

```java
fixtureMonkey.giveMeBuilder(Person.class)
        .set("hobbies.hobby",hobbyArbitrary) // where hobbies is a nested field
```

### Common formatted data

**Email**

- Can use Web Module (`jqwik-web`) to generate data of email format from
    - Create `EmailArbitrary`  using `Web.emails()`

**Time**

- Can use Time Module(`jqwik-time`) to generate date/time-related java types

## Considerations

### Bean validation

- Bean validations will be overridden with Arbitraries
    - E.g.: Cannot use `@Email` on a field that we are going to overrride with Arbitraries (such
      as `Arbitraries.strings().ofMaxLength(20)`)), it will not be possible to generate mock data
      the fits the `@Email` constraint yet also
      abiding to the Arbitraries attributes

### Field name type safety

- To override / custom generator for different fields, we have to pass in the `fieldName` in string,
  like

```java
 ArbitraryBuilder<Person> personArbitraryBuilder=fixtureMonkey.giveMeBuilder(Person.class)
        .set("gender",Arbitraries.of(genderArbitrary))
        ...
```

    - There are thus risks that the field name may not be valid / present in the object to be mocked

- Create Fixture Monkey with `useExpressionStrictMode = true` option to throw exception if the
  property does not exist

## Leftover

### `@Pattern` does not seem to work

- I believe is similar issue
  as [Github issue on Jarkata Validation not working](https://github.com/naver/fixture-monkey/issues/713)

```java
@Pattern(regexp = "^[FM]?$") // javax.validation.constraints.Pattern
private String gender;
```

- Threw exception, which indicates mocked data was not created based off the `@Pattern` constraint

```log
22:25: 48.792 [main] ERROR c.n.f.r.ArbitraryValue$MonkeyRandomGenerator - Fail to create valid arbitrary.

Fixture factory Constraint Violation messages.

- violation: must match "^[FM]?$", type: class com.lqr.javafixture.domain.Person, property: updatedBy, invalidValue: ^M$

javax.validation.ConstraintViolationException: DefaultArbitrayValidator ConstraintViolations. type: class com.lqr.javafixture.domain.Person
...
net.jqwik.api.TooManyFilterMissesException: Filtering [net.jqwik.api.RandomGenerator$$Lambda$634/0x0000000800df30e0@9a9aa68] missed more than 10000 times.
```

# Thoughts

- Lack of documentations for `fixture monkey` components:
    - Some of the useful components such
      as `InnerSpec`, `ArbitraryExpression`, `ExpressionGenerator` may be powerful, but the lack of
      documentations make it difficult to implement and make use of
- Fixture Monkey is heavily reliant on `Jqwik` in the creation of `Arbitrary`
    - `Jqwik` is a property-based testing framework for java
    - Here is a good article that summarize the key features
      of `jqwik`: https://www.baeldung.com/java-jqwik-property-based-testing
    - This means that apart from generating of mock data (with `Arbitrary` + `Fixture Monkey`), we
      are also able to make use of `jqwik` to perform property-based tests which is a complement to
      what we already set up for mocking of data
- Complexities in simple use cases
    - In order to override a particular
- Flexibility in independent field-level constraints
    - The support for Bean Validations (`javax`, `jakarta`), `jqwik` as well as self-made
      annotations provide much flexibility for having custom logic in generating data in fields
    - However, this can only be done on the class itself, where cases if we do not want to "dirty"
      the POJO (e.g.: to segregate mock data constraints and actual domain constraints), we will
      need to duplicate the class itself OR to go for another approach for custom generation
        - Another approach would be through the use of `Arbitrary` which can result in too much
          boilerplate codes & steeper learning curve

# Resources

- https://naver.github.io/fixture-monkey/docs/v0.5/components/arbitrary/
- https://jqwik.net/docs/current/user-guide.html
