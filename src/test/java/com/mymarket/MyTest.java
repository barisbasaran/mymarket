package com.mymarket;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class MyTest {

    @Test
    public void test() {
        var token = UUID.randomUUID().toString();
        System.out.println(token);
    }
}
