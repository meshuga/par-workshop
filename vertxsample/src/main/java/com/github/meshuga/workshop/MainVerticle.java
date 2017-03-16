package com.github.meshuga.workshop;

import com.github.meshuga.workshop.http.HttpService;
import io.vertx.core.Future;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.http.HttpServer;
import rx.Single;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> fut) {
        Single<HttpServer> httpServerObservable = vertx
                .createHttpServer()
                .requestHandler(new HttpService(vertx))
                .rxListen(8080);

        Single<String> stackObservable = vertx
                .rxDeployVerticle("service:com.github.meshuga.workshop.stack");

        Single.zip(httpServerObservable, stackObservable, (r1, r2) -> null)
                .subscribe(v -> fut.complete(), fut::fail);
    }
}