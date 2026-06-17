package com.daren.test;

import java.util.concurrent.Callable;

public class Closure {
    public static void main(String[] args) throws Exception {
        String s = "Hello World!";
        Callable<String> callable = () -> s;
        System.out.println(callable.call());
    }
}
