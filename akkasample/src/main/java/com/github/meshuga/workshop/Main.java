package com.github.meshuga.workshop;

import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static akka.pattern.Patterns.ask;

public class Main {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("problems-system");

        BiFunction<ActorRefFactory, Question, ActorRef> childActorFactory = (refFactory, question) ->
                refFactory.actorOf(OmnipotentSolver.props(question)
                        .withDispatcher("solver-dispatcher"));

        ActorRef problemsService =
                system.actorOf(ProblemsService.props(childActorFactory), "problems-service");

        Timeout timeout = Timeout.apply(1, TimeUnit.DAYS);
        try (Stream<String> stream = Files.lines(Paths.get("questions.txt"))) {
            stream.forEach(q -> ask(problemsService, new Question(q), timeout));
        }

        Await.ready(system.whenTerminated(), Duration.Inf());
    }
}
