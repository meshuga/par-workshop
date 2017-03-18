package com.github.meshuga.workshop;

import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ProblemsService extends AbstractLoggingActor {

    private final BiFunction<ActorRefFactory, Question, ActorRef> childActorFactory;
    private Map<Question, ActorRef> currentQuestions = new HashMap<>();

    public static Props props(BiFunction<ActorRefFactory, Question, ActorRef> childActorFactory) {
        return Props.create(ProblemsService.class, childActorFactory);
    }

    public ProblemsService(BiFunction<ActorRefFactory, Question, ActorRef> childActorFactory) {
        this.childActorFactory = childActorFactory;
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(2, Duration.Inf(), SupervisorStrategy.defaultDecider());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Question.class, this::questionReceived)
                .match(OmnipotentSolver.Result.class, this::resultReceived)
                .match(Terminated.class, this::childTerminated)
                .build();
    }

    private void childTerminated(Terminated t) {
        // TODO add child termination handling
        checkIfQuit();
    }

    private void questionReceived(Question question) {
        ActorRef worker = childActorFactory.apply(getContext(), question);
        context().watch(worker);
        currentQuestions.put(question, worker);
    }

    private void resultReceived(OmnipotentSolver.Result result) {
        currentQuestions.remove(result.getQuestion());
        log().info("Answer to '{}' is '{}'",
                result.getQuestion().getValue(), result.getAnswer());
        log().info("Left {} questions", currentQuestions.size());

        checkIfQuit();
    }

    private void checkIfQuit() {
        if (currentQuestions.size() == 0) {
            log().info("My job is done, bang!");
            context().system().terminate();
        }
    }
}
