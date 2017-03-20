package completable;

import common.BaseTest;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class Completable extends BaseTest {

    @Test
    public void simple() throws Exception {
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> "Hello world!", executor);

        println(future.get());
    }

    @Test
    public void combining() throws Exception {
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> "Hello world!", executor);

        CompletableFuture<String> stringCompletableFuture = future.thenApply((str) -> str + "!!");

        println(stringCompletableFuture.get());
    }

    @Test
    public void composing() throws Exception {
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(() -> "Hello world!", executor);

        CompletableFuture<String> furtherFuture = future.thenCompose((str) ->
                CompletableFuture.supplyAsync(() -> str + " Good morning", executor));

        println(furtherFuture.get());
    }

    @Test
    public void promise() throws Exception {
        CompletableFuture<String> promise = new CompletableFuture<>();

        CompletableFuture<String> furtherFuture = promise.thenCompose((str) ->
                CompletableFuture.supplyAsync(() -> str + " Good morning", executor));

        promise.complete("Hello world!");

        println(furtherFuture.get());
    }

    @Test
    public void exception() throws Exception {
        CompletableFuture<String> promise = new CompletableFuture<>();

        promise.completeExceptionally(new RuntimeException());

        promise.get();
    }

    @Test
    public void exceptionHandling() throws Exception {
        CompletableFuture<String> promise = new CompletableFuture<>();

        promise.completeExceptionally(new RuntimeException());

        CompletableFuture<String> handledResult = promise.handle((str, ex) -> {
            if (ex != null) {
                return "Handled exception: " + ex;
            } else {
                return "The result is: " + str;
            }
        });

        println(handledResult.get());
    }

    @Test
    public void exceptionRecovering() throws Exception {
        CompletableFuture<String> promise = new CompletableFuture<>();

        promise.completeExceptionally(new RuntimeException());

        CompletableFuture<String> handledResult = promise
                .exceptionally((ex) -> "Handled exception: " + ex);

        println(handledResult.get());
    }

}
