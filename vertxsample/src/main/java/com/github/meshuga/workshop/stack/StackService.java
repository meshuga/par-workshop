package com.github.meshuga.workshop.stack;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.eventbus.EventBus;
import io.vertx.rxjava.core.eventbus.Message;
import io.vertx.rxjava.ext.web.client.WebClient;
import io.vertx.rxjava.ext.web.codec.BodyCodec;

public class StackService {
    public static final String GET_STACK_QUESTION = "get-stack-question";

    private static final String uri = "/2.2/questions?pagesize=1&order=desc&sort=activity&site=stackoverflow";

    private final WebClient client;
    private final String host;

    public StackService(Vertx vertx, JsonObject config) {
        client = WebClient.create(vertx, new WebClientOptions()
                .setTryUseCompression(true));
        host = config.getString("host");
    }

    public void register(EventBus eventBus) {
        eventBus.consumer(GET_STACK_QUESTION, this::handleRequest);
    }

    private void handleRequest(Message<Object> msg) {
        client
                .get(host, uri)
                .as(BodyCodec.jsonObject())
                .rxSend()
                .subscribe(response -> {
                    // extract response
                    msg.reply("");
                }, err -> msg.reply("error"));
    }
}
