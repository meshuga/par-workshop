package com.github.meshuga.workshop.stack;

import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;

public class StackVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        new StackService(vertx, config())
                .register(vertx.eventBus());

        super.start(startFuture);
    }
}
