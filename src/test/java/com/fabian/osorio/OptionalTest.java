package com.fabian.osorio;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalTest {

    @Test
    void createAOptional(){
        Optional<String> optValue = Optional.of("name");
        assertEquals("name", optValue.orElse("no name"));
    }

    @Test
    void ofMethodIsNotSafe(){
        assertThrows(NullPointerException.class, () -> Optional.of(null));
    }

    @Test
    void ofNullableMethodIsSafe(){
        Optional<String> optValue = Optional.ofNullable(null);
        assertEquals("no name", optValue.orElse("no name"));
    }

    @Test
    void usingADefaultValueOfOptional(){
        Optional<String> optValue = Optional.empty();
        assertEquals("no name", optValue.orElse("no name"));
    }

    @Test
    void getMethodIsNotSafe(){
        Optional<String> optValue = Optional.empty();
        assertThrows(NoSuchElementException.class, () -> optValue.get());
    }

    @Test
    void isPresentMethod(){
        Optional<String> optValue1 = Optional.empty();
        assertFalse(optValue1.isPresent());
        Optional<String> optValue2 = Optional.ofNullable("value");
        assertTrue(optValue2.isPresent());
    }

    @Test
    void ifPresentMethod(){
        Optional<String> optValue1 = Optional.empty();
        Consumer<String> failTest = value -> fail();
        optValue1.ifPresent(failTest);

        Optional<String> optValue2 = Optional.ofNullable("value");
        Consumer<String> validateValue = value -> assertEquals("value", value);
        optValue2.ifPresent(validateValue);
    }

    @Test
    void filterMethod(){
        Predicate<String> checkValue = value -> value == "value";

        Optional<String> optValue1 = Optional.empty();
        assertFalse(optValue1.filter(checkValue).isPresent());

        Optional<String> optValue2 = Optional.ofNullable("value");
        assertTrue(optValue2.filter(checkValue).isPresent());

        Optional<String> optValue3 = Optional.empty();
        assertThrows(NullPointerException.class, () ->optValue3.filter(null)); // It is not safe
    }

    @Test
    void mapFunction(){
        Optional<Integer> optValue1 = Optional.ofNullable(3);
        Optional<Integer> optValue2 = Optional.empty();

        Function<Integer, Integer> duplicate = value -> value * 2;

        assertEquals(Optional.of(6), optValue1.map(duplicate));
        assertEquals(Optional.empty(), optValue2.map(duplicate));
    }

    @Test
    void flatMapFunction(){
        Optional<Integer> optValue1 = Optional.ofNullable(3);
        Optional<Integer> optValue2 = Optional.ofNullable(2);
        Optional<Integer> optValue3 = Optional.empty();

        Function<Integer, Optional<Integer>> duplicateIfEven = value ->
                (value % 2 == 0) ? Optional.of(value * 2) : Optional.empty();

        assertEquals(Optional.empty(), optValue1.flatMap(duplicateIfEven));
        assertEquals(Optional.of(4), optValue2.flatMap(duplicateIfEven));
        assertEquals(Optional.empty(), optValue3.flatMap(duplicateIfEven));
    }

    @Test
    void orElseGetFunction(){
        Optional<Integer> optValue1 = Optional.ofNullable(3);
        Optional<Integer> optValue2 = Optional.empty();

        Supplier<Integer> defaultValue = () -> 0;

        assertEquals(3, optValue1.orElseGet(defaultValue));
        assertEquals(0, optValue2.orElseGet(defaultValue));
    }

    @Test
    void orElseThrowFunction(){
        Optional<Integer> optValue1 = Optional.ofNullable(3);
        Optional<Integer> optValue2 = Optional.empty();

        Supplier<NoSuchElementException> defaultValue = () -> new NoSuchElementException("empty");

        assertEquals(3, optValue1.orElseThrow(defaultValue));
        assertThrows(NoSuchElementException.class, () -> optValue2.orElseThrow(defaultValue));
    }




}
