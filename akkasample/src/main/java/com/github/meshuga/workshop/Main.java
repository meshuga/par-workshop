package com.github.meshuga.workshop;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static akka.pattern.Patterns.ask;

public class Main {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("problems-system");
        ActorRef problemsService =
                system.actorOf(Props.create(ProblemsService.class), "problems-service");

        Timeout timeout = Timeout.apply(1, TimeUnit.DAYS);
        try (Stream<String> stream = Files.lines(Paths.get("questions.txt"))) {
            stream.forEach(q -> ask(problemsService, new Question(q), timeout));
        }

        Await.ready(system.whenTerminated(), Duration.Inf());
    }
}
