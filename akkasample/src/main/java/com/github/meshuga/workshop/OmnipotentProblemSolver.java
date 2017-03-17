package com.github.meshuga.workshop;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class OmnipotentProblemSolver extends AbstractLoggingActor {

    private static final String blockedQuestion = "Why does time have a direction?";
    private static final Start START = new Start();

    private final Question question;
    private final ActorRef parent;

    public OmnipotentProblemSolver(Question question, ActorRef parent) {
        this.question = question;
        this.parent = parent;
    }

    public static Props props(Question question, ActorRef parent) {
        return Props.create(OmnipotentProblemSolver.class, question, parent)
                .withDispatcher("solver-dispatcher");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Start.class, msg -> msg == START, v -> startWorking())
                .build();
    }

    @Override
    public void preStart() throws Exception {
        self().tell(START, null);
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        self().tell(START, null);
    }

    private void startWorking() throws Exception {
        if (blockedQuestion.equals(question.getValue())) {
            // TODO uncomment to start throwing exceptions
//            throw new RuntimeException("Sorry boys");
        }
        log().info("Starting to work on: {}", question.getValue());
        Thread.sleep(2_000);
        parent.tell(new Result(question, "42"), self());
    }

    private static class Start {

    }

    public static class Result {
        private final Question question;
        private final String answer;

        public Result(Question question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public Question getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
