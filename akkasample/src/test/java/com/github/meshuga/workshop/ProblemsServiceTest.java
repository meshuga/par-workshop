package com.github.meshuga.workshop;

import akka.actor.ActorRef;
import akka.actor.ActorRefFactory;
import org.junit.Test;

import java.util.function.BiFunction;

public class ProblemsServiceTest extends BaseTest {

    @Test
    public void onQuestionShouldCreateChild() throws Exception {
        // GIVEN
        BiFunction<ActorRefFactory, Question, ActorRef> childActorFactory = null;

        // TODO finish test
    }
}
