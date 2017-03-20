package stream;

import common.BaseTest;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Stream;

public class Streams extends BaseTest {

    @Test
    public void simple() throws Exception {
        ForkJoinTask<Optional<Integer>> task = forkJoinPool.submit(() -> Stream.of(1, 2, 3)
                .parallel()
                .reduce((a, b) -> a + b));

        Optional<Integer> result = task.get();

        println(result);
    }
}
