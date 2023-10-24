package com.fabian.osorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.*;

import static org.junit.jupiter.api.Assertions.*;

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

    // some typical functional interfaces: Consumer, Supplier, Predicate, and Function

    /*
    Consumer represents an operation that accepts an argument and performs some action without returning any result.
    Common use cases include printing, updating, or processing data.
     */
    @Test
    void consumerFunctionalInterface() {
        Consumer<String> printName = name -> System.out.println("hello: " + name);
        people.stream()
                .map(Person::getName)
                .forEach(printName);
        assertTrue(true);
    }

    /*
    Supplier represents a supplier of results and doesn't accept any arguments. It provides a value.
    Common use cases include lazy initialization, generating unique identifiers, and more.
     */
    @Test
    void supplierFunctionalInterface(){
        Supplier<Integer> randomNumber = () -> new Random().nextInt(10);
        Integer firstRandomValue = randomNumber.get();
        Integer secondRandomValue = randomNumber.get();
        assertNotNull(firstRandomValue);
        assertNotNull(secondRandomValue);
        assertTrue(firstRandomValue < 11);
        assertTrue(secondRandomValue < 11);
    }

    interface MySupplierThread {
        Thread getCurrentThread();
    }

    @Test
    void methodReferenceCouldBeAssignToAnyInterface(){
        Supplier<Thread> s1 = Thread::currentThread; // static method to Supplier
        MySupplierThread s2 = Thread::currentThread; // static method to my own interface
        assertEquals(s1.get().getName(), s2.getCurrentThread().getName());
    }

    /*
    Predicate represents a boolean-valued function of one argument. It's used for making decisions, filtering, and testing conditions.
    Common use cases include filtering collections and data validation.
     */
    @Test
    void predicateFunctionalInterface(){
        Predicate<Person> greaterThan20 = person -> person.getAge() > 20;
        Long numberOfPeople = people.stream()
                .filter(greaterThan20)
                .count();
        assertEquals(3L, numberOfPeople);
    }

    /*
    Function represents a function that takes one argument and produces a result.
    Common use cases include data transformation, mapping, and conversion.
     */
    @Test
    void functionFunctionalInterface(){
        //Function<Person, Integer> getAges = person -> person.getAge();
        Function<Person, Integer> getAges = Person::getAge;
        ToIntFunction<Integer> toInt = Integer::intValue;
        Integer sumAges = people.stream()
                .map(getAges)
                .mapToInt(toInt)
                .sum();
        assertEquals(101, sumAges);
    }

}
