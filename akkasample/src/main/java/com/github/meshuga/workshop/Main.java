package com.github.meshuga.workshop;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import scala.compat.java8.FutureConverters;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static akka.pattern.Patterns.ask;

public class Main {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("problems-system");
        ActorRef problemsService =
                system.actorOf(Props.create(ProblemsService.class), "problems-service");

        Timeout timeout = Timeout.apply(1, TimeUnit.DAYS);
        try (Stream<String> stream = Files.lines(Paths.get("questions.txt"))) {
            List<CompletableFuture<Object>> questions = stream
                    .map(q -> ask(problemsService, new Question().setValue(q), timeout))
                    .map(f -> FutureConverters.toJava(f).toCompletableFuture())
                    .collect(Collectors.toList());
        }
    }
}
