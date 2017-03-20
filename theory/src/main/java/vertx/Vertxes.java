package vertx;

import common.BaseTest;
import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.Message;
import org.junit.Test;

public class Vertxes extends BaseTest {

    @Test
    public void simpleVerticle() throws Exception {
        Vertx vertx = Vertx.vertx();

        vertx.getDelegate().deployVerticle(new FirstVerticle(), (res) ->
                latch.countDown()
        );
        latch.await();

        Message<Object> message = vertx.eventBus()
                .rxSend("address", "Hello world!")
                .toBlocking()
                .value();
        println(message.body());
    }

    public static class FirstVerticle extends AbstractVerticle {

        @Override
        public void start(Future<Void> startFuture) throws Exception {
            vertx.eventBus().consumer("address", (msg) -> {
                String body = (String) msg.body();
                msg.reply("Verticle got: " + body);
            });
            super.start(startFuture);
        }
    }
}
