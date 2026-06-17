package com.daren.test;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {
    public static void main(String[] args) {

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(() -> "hello world!");
//        stringCompletableFuture.thenRun()
        CompletableFuture<Void> voidCompletableFuture = stringCompletableFuture.thenApply((s) -> s + s).thenAccept(System.out::println).thenApplyAsync(s -> s);
        voidCompletableFuture.thenAccept(System.out::println);
    }
}
