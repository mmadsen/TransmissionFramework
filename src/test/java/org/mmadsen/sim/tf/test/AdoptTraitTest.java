package org.mmadsen.sim.tf.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IModelGlobals;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.traits.UnstructuredTrait;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 3:22:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdoptTraitTest {

    ITrait testTrait, testTrait2;
    IAgent testAgent;
    IModelGlobals model;

    @Before
    public void setup() {
        this.model = new ModelFixture();
        this.testTrait = new UnstructuredTrait("TestTrait", this.model);
        this.testAgent = new AgentFixture(1);
    }

    @After
    public void cleanup() {
        this.testTrait = null;
        this.testAgent = null;
    }

    @Test
    public void simpleAdoption() {
        this.testTrait.adopt(this.testAgent);
        Integer adoptCount = this.testTrait.getCurrentAdoptionCount();
        //this.model.getModelLogger().info("adoption count: " + adoptCount);
        Assert.assertTrue(adoptCount == 1);
    }

    @Test
    public void multipleAdoption() {
        this.testTrait.adopt(this.testAgent);
        for(int i = 2; i < 15; i++) {
            //this.model.getModelLogger().info("i: " + i);
            this.testTrait.adopt(new AgentFixture(i));
        }
        IAgent lastAgent = new AgentFixture(15);
        this.testTrait.adopt(lastAgent);

        Assert.assertTrue(this.testTrait.getCurrentAdoptionCount() == 15);

        this.testTrait.unadopt(this.testAgent);
        this.testTrait.unadopt(lastAgent);

        Assert.assertTrue(this.testTrait.getCurrentAdoptionCount() == 13);

    }



}
