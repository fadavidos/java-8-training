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

    class ClassC implements InterfaceOne {

        @Override
        public String getName() {
            return "ClassC";
        }

        @Override
        public String getDescription() {
            return "ClassC";
        }
    }

    @Test
    void addADefaultMethod(){
        ClassA classA = new ClassA();
        assertEquals("InterfaceOne", classA.getDescription());
    }
    @Test
    void forceToImplementADefaultMethod(){
        ClassB classB = new ClassB();
        assertEquals("ClassB", classB.getDescription());
    }

    @Test
    void overrideDefaultMethod(){
        ClassC classC = new ClassC();
        assertEquals("ClassC", classC.getDescription());
    }


}
