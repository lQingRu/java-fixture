package com.lqr.javafixture.domain;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@Getter
@ToString
public class Hobby {

    // Free-text
    @NotBlank
    private String hobby;

    // Range of numbers (1 to 5)
    @Min(1)
    @Max(7)
    private int interestLevel;
}
