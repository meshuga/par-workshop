package com.github.meshuga.workshop.http;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServerRequest;

import java.util.Collections;

import static com.github.meshuga.workshop.stack.StackService.GET_STACK_QUESTION;

public class HttpService implements Handler<HttpServerRequest> {

    private final Vertx vertx;

    public HttpService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void handle(HttpServerRequest request) {
        vertx.eventBus().rxSend(GET_STACK_QUESTION, null)
                .subscribe(msg -> {
                    String responseData = new JsonObject(Collections
                            .singletonMap("QuestionTitle", msg.body())).toString();
                    request.response()
                            .putHeader("Content-Type", "application/json")
                            .end(responseData);
                });
    }
}
