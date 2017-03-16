package com.github.meshuga.workshop;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;

import java.util.HashMap;
import java.util.Map;

public class ProblemsService extends AbstractLoggingActor {

    private Map<Question, ActorRef> currentQuestions = new HashMap<>();

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Question.class, question -> {
                    ActorRef worker = getContext().actorOf(OmnipotentProblemSolver.props());
                    worker.tell(question, self());
                    currentQuestions.put(question, worker);
                })
                .match(OmnipotentProblemSolver.Result.class, r -> {
                    currentQuestions.remove(r.getQuestion());
                    log().info("Answer to '{}' is '{}'",
                            r.getQuestion().getValue(), r.getAnswer());
                    log().info("Left {} questions", currentQuestions.size());
                })
                .build();
    }
}
