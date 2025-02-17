package com.ecommers.serviceuser.annatation;

import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract public class ParentTestInstant {

    public int counter = 0;

}
