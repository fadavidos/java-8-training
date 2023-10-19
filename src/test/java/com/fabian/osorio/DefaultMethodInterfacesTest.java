package com.fabian.osorio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultMethodInterfacesTest {

    interface InterfaceOne {
        String getName();
        default String getDescription(){
            return "InterfaceOne";
        }
    }

    interface InterfaceTwo extends InterfaceOne {
        /*
        Any class that implements the interface InterfaceTwo will have to implement the method getDescription;
        This method is an abstract method like all other non-default (and non-static) methods in an interface.
         */
        String getDescription();
    }

    class ClassA implements InterfaceOne {
        @Override
        public String getName() {
            return "ClassA";
        }
    }

    class ClassB implements InterfaceTwo {

        @Override
        public String getName() {
            return "ClassB";
        }

        @Override
        public String getDescription() {
            return "ClassB";
        }
    }

    @Test
    void overrideDefaultMethod(){
        ClassB classB = new ClassB();
        ClassA classA = new ClassA();
        assertEquals("ClassB", classB.getDescription());
        assertEquals("InterfaceOne", classA.getDescription());
    }
}
