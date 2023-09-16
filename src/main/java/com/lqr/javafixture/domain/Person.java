package com.lqr.javafixture.domain;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import java.util.List;

@ToString
@Getter
@Data
public class Person {

    private long id;

    // 1. Set of fixed values
    private String gender;

    // 2. Enum
    private ProfileType profileType;

    // 3. String formatter
    private String updatedBy;

    // 4. Free Text
    private String name;

    // 5. List of allowable values
    private List<String> countries;

    // 6. List of nested objects
    private List<Hobby> hobbies;

    @Email
    private String email;

}
