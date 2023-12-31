package com.onlinebookstore.catalogservice.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class BookValidationTests {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        Book book = Book.of("1234567890", "Title", "Author", 9.90, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnNotDefinedThenValidationFails() {
        Book book = Book.of("", "Title", "Author", 9.90, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        List<String> constraintViolationMessages = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        assertThat(constraintViolationMessages)
                .contains("The book ISBN must be defined.")
                .contains("The ISBN format must be valid.");

    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = Book.of("1234567890", "Title", "Author", 9.90, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid");
    }

    @Test
    void whenTitleIsNotDefinedThenValidationFails() {
        Book book = Book.of("1234567890", "", "Author", 9.90, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book title must be defined");
    }

    @Test
    void whenAuthorIsNotDefinedThenValidationFails() {
        Book book = Book.of("1234567890", "Title", "", 9.90, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book author must be defined.");
    }

    @Test
    void whenPriceIsNotDefinedThenValidationFails() {
        Book book = Book.of("1234567890", "Title", "Author", null, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be defined.");
    }

    @Test
    void whenPriceDefinedButZeroThenValidationFails() {
        Book book = Book.of("1234567890", "Title", "Author", 0.0, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

    @Test
    void whenPriceDefinedButNegativeThenValidationFails() {
        Book book = Book.of("1234567890", "Title", "Author", -9.90, "Bookstore");
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The book price must be greater than zero.");
    }

    @Test
    void whenPublisherIsNotDefinedThenValidationSucceeds() {
        Book book = Book.of("1234567890", "Title", "Author", -9.90, null);
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

}
