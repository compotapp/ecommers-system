package com.ecommers.serviceuser.annatation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class PerMethodTestInstant extends ParentTestInstant {

    @BeforeEach
    void setUp() {
        counter++;
    }

    @Test
    void test1() {
        assertEquals(1, counter);
    }

    @Test
    void test2() {
        assertEquals(1, counter);
    }
}
