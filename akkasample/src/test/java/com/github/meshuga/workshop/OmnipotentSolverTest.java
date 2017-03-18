package com.github.meshuga.workshop;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import org.junit.Test;

import static akka.testkit.JavaTestKit.duration;

public class OmnipotentSolverTest extends BaseTest {

    @Test
    public void onStartShouldStartWorking() throws Exception {
        // GIVEN
        final JavaTestKit probe = new JavaTestKit(system);

        Question question = new Question("Is 2+2 equal 4?");
        final Props props = Props.create(OmnipotentSolver.class, question);

        // WHEN
        probe.childActorOf(props);

        // THEN
        probe.expectMsgAnyClassOf(duration("3 second"), OmnipotentSolver.Result.class);
    }
}
