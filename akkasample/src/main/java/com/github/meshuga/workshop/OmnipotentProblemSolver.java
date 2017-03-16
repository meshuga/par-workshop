package com.github.meshuga.workshop;

import akka.actor.AbstractLoggingActor;
import akka.actor.Props;

public class OmnipotentProblemSolver extends AbstractLoggingActor {

    public static Props props() {
        return Props.create(OmnipotentProblemSolver.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Question.class, question ->
                        sender().tell(new Result()
                        .setQuestion(question)
                        .setAnswer("42"), self()))
                .build();
    }

    public static class Result {
        private Question question;
        private String answer;

        public Question getQuestion() {
            return question;
        }

        public Result setQuestion(Question question) {
            this.question = question;
            return this;
        }

        public String getAnswer() {
            return answer;
        }

        public Result setAnswer(String answer) {
            this.answer = answer;
            return this;
        }
    }
}
