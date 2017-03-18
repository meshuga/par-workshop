package com.github.meshuga.workshop;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class BaseTest {

    protected static ActorSystem system;

    @BeforeClass
    public static void setupBaseTest() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardownBaseTest() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }
}
