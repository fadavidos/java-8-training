package com.fabian.osorio;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class StreamsTest {

    @Test
    void sumListOfIntegers() {
        List<Integer> list = Stream.iterate(1, n -> n + 1)
                .limit(10)
                .collect(Collectors.toList());

        Integer total = list.stream()
                .mapToInt(Integer::intValue)
                .sum();

        assertEquals(55, total);
    }

    @Test
    void countListOfElements(){
        List<Integer> list = Stream.iterate(1, n-> n +1)
                .limit(10)
                .collect(Collectors.toList());

        Long count = list.stream().count();

        assertEquals(10, count);

    }

    @Test
    void sumElementsUsingMethodReference(){
        List<Integer> list = Stream.iterate(1, n -> n +1)
                .limit(10)
                .collect(Collectors.toList());
        Integer sum = list.stream()
                //.reduce(0, (total, element) -> Integer.sum(total, element));
                .reduce(0, Integer::sum);
        assertEquals(55, sum);

    }

    @Test
    void concatElementsUsingMethodReference(){
        List<Integer> list = Stream.iterate(1, n -> n + 1)
                .limit(10)
                .collect(Collectors.toList());

        String concatValue = list.stream().map(String::valueOf)
                //.reduce("", (concat, str) -> concat.concat(str));
                .reduce("", String::concat);

        assertEquals("12345678910", concatValue);
    }

    @Test
    void usingFilterAndMapToIntInStream(){
        List<Integer> list = Stream.iterate(1, n -> n + 1)
                .limit(10)
                .collect(Collectors.toList());

        Predicate<Integer> isEven = n -> n % 2 == 0;
        ToIntFunction<Integer> duplicate = e -> e * 2;

        int sum = list.stream()
                .filter(isEven)
                .mapToInt(duplicate)
                .sum();

        assertEquals(60, sum);
    }

    double executeComplexCode(Runnable block) {
        long start = System.nanoTime();
        try{
            block.run();
            long end = System.nanoTime();
            return (end -start)/1.0e9;
        } catch (Exception ex){
            long end = System.nanoTime();
            System.out.println("time taken(s): " + (end -start)/1.0e9);
        }
        return 0D;
    }

    @Test
    void parallelStreams(){
        List<Integer> list = Stream.iterate(1, n -> n +1)
                .limit(10)
                .collect(Collectors.toList());

        Predicate<Integer> isEven = n -> n % 2 == 0;
        Consumer<Integer> delay = time -> {try {Thread.sleep(time);}catch (Exception ignored){}};
        ToIntFunction<Integer> duplicateWithADelay = element -> {
                delay.accept(1000);
                return element * 2;
        };

        double timeSequentially = executeComplexCode(() ->
                list.stream()
                    .filter(isEven)
                    .mapToInt(duplicateWithADelay)
                    .sum()
        );

        double timeParallel = executeComplexCode(() ->
                list.parallelStream()
                    .filter(isEven)
                    .mapToInt(duplicateWithADelay)
                    .sum()
        );

        System.out.printf("Time for timeSequentially: %s%n", timeSequentially);
        System.out.printf("Time for timeParallel: %s", timeParallel);

        assertTrue(timeSequentially >= timeParallel);
    }

    @Test
    void createASetFromStream(){
        List<Integer> numbers = Arrays.asList(1,2,3,4,5);
        Predicate<Integer> isEven = num -> num % 2 == 0;
        Function<Integer, Double> duplicate = num -> num * 2.0;
        Set<Double> doubleOfEven = numbers.stream()
                .filter(isEven)
                .map(duplicate)
                .collect(Collectors.toSet());
        assertEquals(new HashSet<>(Arrays.asList(8.0, 4.0)), doubleOfEven);
    }

    @Test
    void createAMapFromStream(){
        List<Person> people = new ArrayList<>();
        people.add(new Person("1", "Juan", 20));
        people.add(new Person("2", "Peter", 25));
        people.add(new Person("3", "Liss", 28));
        people.add(new Person("4", "Conor", 28));

        Map<String, Person> mapPeople = people.stream()
                .collect(Collectors.toMap(
                        //p -> p.getId(), || p -> p.getAge() + "-" + p.getName()
                        Person::getId,
                        p -> p
                ));

        assertEquals(mapPeople.get("1").getName(), "Juan");
        assertEquals(mapPeople.get("3").getName(), "Liss");
    }

    @Test
    void groupingStream(){
        List<Person> people = new ArrayList<>();
        people.add(new Person("1", "Juan", 20));
        people.add(new Person("2", "Peter", 25));
        people.add(new Person("3", "Liss", 22));
        people.add(new Person("4", "Conor", 28));
        people.add(new Person("5", "Juan", 72));
        people.add(new Person("6", "Liss", 46));
        people.add(new Person("7", "Juan", 35));

        Map<String, List<Integer>> result = people.stream()
                .collect(
                        Collectors.groupingBy(
                                //p -> p.getName(),
                                Person::getName,
                                Collectors.mapping(
                                        //per -> per.getAge(),
                                        Person::getAge,
                                        Collectors.toList()
                                )
                        )
                );
        assertEquals(3, result.get("Juan").size());
        assertEquals(2, result.get("Liss").size());
    }

    @Test
    void groupingStreamComplex() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("1", "Juan", 20, "M", 110.0));
        people.add(new Person("2", "Peter", 25, "M", 400.0));
        people.add(new Person("3", "Liss", 22, "F", 120.0));
        people.add(new Person("4", "Conor", 20, "F", 100.0));
        people.add(new Person("5", "Juan", 25, "M", 140.0));
        people.add(new Person("6", "Liss", 22, "F", 150.0));
        people.add(new Person("7", "Juan", 20, "M", 120.0));

        // group people by their age, but within each age group, you want to further group them by their gender.
        // Finally, you want to calculate the average salary for each group.

        Map<Integer, Map<String, Double>> result = people.stream()
                .collect(Collectors.groupingBy(
                        Person::getAge,
                        Collectors.groupingBy(
                                Person::getGender,
                                Collectors.averagingDouble(Person::getSalary)
                        )
                ));

        assertEquals(115.0, result.get(20).get("M"));
        assertEquals(100.0, result.get(20).get("F"));
        assertNull(result.get(22).get("M"));
        assertEquals(135.0, result.get(22).get("F"));
        assertEquals(270.0, result.get(25).get("M"));
        assertNull(result.get(25).get("F"));
    }

    @Test
    void joiningStream(){
        List<Integer> numbers = Arrays.asList(1,2,3,4,5);
        String concatenated = numbers.stream()
                .map(String::valueOf)
                .collect(Collectors.joining("-"));
        assertEquals("1-2-3-4-5", concatenated);
    }



}
