package com.github.meshuga.workshop;

import akka.actor.*;
import scala.concurrent.duration.Duration;

import java.util.HashMap;
import java.util.Map;

public class ProblemsService extends AbstractLoggingActor {

    private Map<Question, ActorRef> currentQuestions = new HashMap<>();

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return new OneForOneStrategy(2, Duration.Inf(), SupervisorStrategy.defaultDecider());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Question.class, this::questionReceived)
                .match(OmnipotentProblemSolver.Result.class, this::resultReceived)
                .match(Terminated.class, this::childTerminated)
                .build();
    }

    private void childTerminated(Terminated t) {
        // TODO add child termination handling
        checkIfQuit();
    }

    private void questionReceived(Question question) {
        ActorRef worker = getContext().actorOf(OmnipotentProblemSolver.props(question, self()));
        context().watch(worker);
        currentQuestions.put(question, worker);
    }

    private void resultReceived(OmnipotentProblemSolver.Result result) {
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
