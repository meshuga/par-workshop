package rxjava;

import common.BaseTest;
import org.junit.Test;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.internal.schedulers.ExecutorScheduler;

import java.util.Arrays;

public class Observables extends BaseTest {

    @Test
    public void simple1() {
        Observable<Integer> observable = Observable.create(subscriber -> {
            for (Integer i : Arrays.asList(1, 2, 3)) {
                if (subscriber.isUnsubscribed()) {
                    return;
                }
                subscriber.onNext(i);

            }
            if (!subscriber.isUnsubscribed()) {
                subscriber.onCompleted();
            }
        });

        Subscription subscribe = observable.subscribe(this::println);
    }

    @Test
    public void simple2() {
        Observable<Integer> observable = Observable.from(Arrays.asList(1, 2, 3));

        observable.subscribe(this::println);
    }

    @Test
    public void map() {
        Observable<Integer> observable = Observable.from(Arrays.asList(1, 2, 3));

        observable
                .map(i -> i + 42)
                .subscribe(this::println);
    }

    @Test
    public void flatMap() {
        Observable<Integer> observable = Observable.from(Arrays.asList(1, 2, 3));

        observable
                .flatMap(i -> Observable.just(10, 20)
                    .map(j -> j + i)
                )
                .subscribe(this::println);
    }

    @Test
    public void errorHandling() {
        Observable<String> observable =
                Observable.error(new RuntimeException("Unexpected error"));

        observable
                .subscribe(this::println, this::println);
    }

    @Test
    public void errorRecovery() {
        Observable<String> observable =
                Observable.error(new RuntimeException("Unexpected error"));

        observable
                .onErrorReturn(ex -> "Got error: " + ex.getMessage())
                .subscribe(this::println);
    }

    @Test
    public void useExecutor() throws Exception {

        Observable<String> observable = Observable.create(subscriber -> {
            println("Thread used to do main task: " + Thread.currentThread());
            subscriber.onNext("hello world!");

            subscriber.onCompleted();
        });
        Scheduler scheduler = new ExecutorScheduler(executor);
        println("Main thread: " + Thread.currentThread());

        observable
                .subscribeOn(scheduler)
                .observeOn(scheduler)
                .subscribe(str -> {
                    println("Thread used to handle subscriber: " + Thread.currentThread());
                    println(str);
                    latch.countDown();
                });

        latch.await();
    }

    @Test
    public void blockingOperation() throws Exception {

        Observable<String> observable = Observable.create(subscriber -> {
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            println("Thread used to do main task: " + Thread.currentThread());
            subscriber.onNext("hello world!");

            subscriber.onCompleted();
        });
        Scheduler scheduler = new ExecutorScheduler(executor);
        println("Main thread: " + Thread.currentThread());

        String string = observable
                .subscribeOn(scheduler)
                .toBlocking()
                .first();

        println(string);
    }
}
