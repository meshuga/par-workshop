package common;

import org.junit.AfterClass;
import org.junit.Before;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public abstract class BaseTest {

    protected static ForkJoinPool forkJoinPool = new ForkJoinPool(2);
    protected static ExecutorService executor = Executors.newFixedThreadPool(4);

    protected CountDownLatch latch;

    @Before
    public void beforeBaseTest() {
        latch = new CountDownLatch(1);
    }

    @AfterClass
    public static void tearDown() {
        forkJoinPool.shutdown();
        executor.shutdown();
    }

    protected void println(Object x) {
        System.out.println(x);
    }
}
