package com.fabian.osorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class LambdaExpressionsTest {

    private List<Person> people = new ArrayList<Person>();

    @BeforeEach
    void setUp(){
        people = new ArrayList<>();
        people.add(new Person("1", "Juan", 20));
        people.add(new Person("2", "Peter", 25));
        people.add(new Person("3", "Liss", 28));
        people.add(new Person("4", "Conor", 28));
    }

    /*
    Aggregate operations in Java are a set of operations that process elements from a stream, such as filtering, mapping, and reducing.
     */
    @Test
    @DisplayName("List should be filtered with aggregate operations that accept lambdas expression as parameters")
    void filterAListUsingAggregateOperations(){
        List<Person> greaterThan25 = people.stream()
                .filter( person -> person.getAge() >= 25)
                .collect(Collectors.toList());
        assertEquals(3, greaterThan25.size());
    }

    /*
    There are two main types of aggregate operations in Java:

    Intermediate Operations: These operations are applied to a stream and return a new stream as their result.
    They are often used for filtering and transforming data.
    Common intermediate operations include filter, map, flatMap, distinct, sorted, and more.

    Terminal Operations:
    These operations consume the elements of a stream and produce a result or a side effect.
    Terminal operations trigger the processing of a stream and are often used to produce a final result,
    such as a single value or a collection. Common terminal operations include forEach, collect, reduce, count, and min/max
     */

    @Test
    void intermediateOperationMap() {
        List<String> namesUpperCase = people.stream()
                .map( person -> person.getName().toUpperCase())
                .collect(Collectors.toList());
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("JUAN");
        expectedNames.add("PETER");
        expectedNames.add("LISS");
        expectedNames.add("CONOR");
        assertIterableEquals(expectedNames, namesUpperCase);
    }

    /*
    @Test
    void lambdasCanReadValuesButTheyCannotChangeThem(){
        String name;
        people.stream()
            .forEach( person -> {
                name = name + person.getName(); // compilation error: Variable used in lambda expression should be final or effectively final
            });
    }
    */




}
