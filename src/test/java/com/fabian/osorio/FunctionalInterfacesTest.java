package com.fabian.osorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionalInterfacesTest {

    private List<Person> people = new ArrayList<Person>();

    // Any interface with a single abstract method
    // Functional interface is pure functional, stateless classes
    @FunctionalInterface
    interface InterfaceOne<T> {
        String toString(T number);
    }

    @BeforeEach
    void setUp(){
        people = new ArrayList<>();
        people.add(new Person("Juan", 20));
        people.add(new Person("Peter", 25));
        people.add(new Person("Liss", 28));
        people.add(new Person("Conor", 28));
    }

    @Test
    @DisplayName("Functional interfaces should allow us to define behavior for each data type")
    void implementFunctionalInterfaceForIntegerAndDouble() {

        InterfaceOne<Integer> integerInterfaceOne = new InterfaceOne<Integer>(){
            @Override
            public String toString(Integer number) {
                return String.valueOf(number);
            }
        };

        InterfaceOne<Double> doubleInterfaceOne = new InterfaceOne<Double>() {
            @Override
            public String toString(Double number) {
                return String.valueOf(number);
            }
        };

        assertEquals("10", integerInterfaceOne.toString(10));
        assertEquals("10.2", doubleInterfaceOne.toString(10.2));
    }


    interface MyFilter<T> {
        boolean apply(T t);
    }

    public static List<Person> filterPeople(List<Person> listOfPeople, MyFilter<Person> filter) {
        List<Person> filteredPeople = new ArrayList<>();
        for(Person p: listOfPeople) {
            if(filter.apply(p)) {
                filteredPeople.add(p);
            }
        }
        return filteredPeople;
    }

    @Test
    @DisplayName("List should be filtered with functional interfaces")
    void filterAListUsingFunctionalInterfaces() {
        people.add(new Person("Jean", 27));
        List<Person> resultOfFilter = filterPeople(
                people,
                person -> person.getAge() > 25 &&
                        person.getName().startsWith("J")
        );
        assertAll("list",
                () -> assertEquals(1, resultOfFilter.size()),
                () -> assertEquals("Jean", resultOfFilter.get(0).getName())
        );
    }
}
