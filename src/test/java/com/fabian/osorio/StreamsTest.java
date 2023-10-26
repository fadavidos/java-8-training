package com.fabian.osorio;

import org.junit.jupiter.api.Test;

import java.util.List;
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


}
