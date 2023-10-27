package com.fabian.osorio;

import org.junit.jupiter.api.Test;

import java.util.List;
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

    double code(Runnable block) {
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

        double timeSequentially = code(() ->
                list.stream()
                    .filter(isEven)
                    .mapToInt(duplicateWithADelay)
                    .sum()
        );

        double timeParallel = code(() ->
                list.parallelStream()
                    .filter(isEven)
                    .mapToInt(duplicateWithADelay)
                    .sum()
        );

        System.out.printf("Time for timeSequentially: %s%n", timeSequentially);
        System.out.printf("Time for timeParallel: %s", timeParallel);

        assertTrue(timeSequentially >= timeParallel);

    }


}
