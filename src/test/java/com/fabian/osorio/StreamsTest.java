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


}
