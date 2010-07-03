package org.mmadsen.sim.tf.test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

import atunit.*;
import atunit.example.subjects.*;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.junit.runner.RunWith;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.traits.UnstructuredTrait;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 4:26:25 PM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class UnstructuredTraitAdoptionTest implements Module  {
    @Inject @Unit ITrait trait;
    @Inject IAgent agent;
    @Inject ISimulationModel model;


    @Test
    public void simpleAdoption() {
        trait.setTraitID("TestTrait1");
        agent.setAgentID("TestAgent1");
        trait.adopt(agent);
        Integer adoptCount = trait.getCurrentAdoptionCount();
        //model.getModelLogger().info("adoption count: " + adoptCount);
        assertTrue(adoptCount == 1);
    }

    @Test
    public void multipleAdoption() {
        trait.adopt(agent);
        for(Integer i = 2; i < 15; i++) {
            IAgent newAgent = new AgentFixture();
            newAgent.setAgentID(i.toString());
            //model.getModelLogger().info("i: " + i);
            trait.adopt(newAgent);
        }
        IAgent lastAgent = new AgentFixture();
        lastAgent.setAgentID("15");
        trait.adopt(lastAgent);

        assertTrue(trait.getCurrentAdoptionCount() == 15);

        trait.unadopt(this.agent);
        trait.unadopt(lastAgent);
        //model.getModelLogger().info("final count: " + trait.getCurrentAdoptionCount());
        assertTrue(trait.getCurrentAdoptionCount() == 13);

    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).to(UnstructuredTrait.class);
        binder.bind(IAgent.class).to(AgentFixture.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
    }
}
