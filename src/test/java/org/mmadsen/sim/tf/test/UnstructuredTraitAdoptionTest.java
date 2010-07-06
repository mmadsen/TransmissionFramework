package org.mmadsen.sim.tf.test;

import static org.junit.Assert.*;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import atunit.*;

import com.google.inject.Binder;
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
    @Inject
    @Unit ITrait trait;
    @Inject IAgent agent;
    @Inject ISimulationModel model;
    Logger log;

    @Before
    public void setup() {
        log = model.getModelLogger(this.getClass());
    }

    @Test
    public void simpleAdoption() {
        log.info("Entering simpleAdoption test");
        trait.setTraitID("TestTrait1");
        agent.setAgentID("TestAgent1");
        trait.adopt(agent);
        Integer adoptCount = trait.getCurrentAdoptionCount();
        log.info("expecting count:  1 observed count: " + adoptCount);
        assertTrue(adoptCount == 1);
        log.info("Exiting simpleAdoption test");
    }

    @Test
    public void multipleAdoption() {
        log.info("Entering multipleAdoption test, adoption by 15 agents");
        trait.adopt(agent);
        for(Integer i = 2; i < 15; i++) {
            IAgent newAgent = new AgentFixture();
            newAgent.setAgentID(i.toString());
            trait.adopt(newAgent);
        }
        IAgent lastAgent = new AgentFixture();
        lastAgent.setAgentID("15");
        trait.adopt(lastAgent);
        log.info("expecting: 15 observed: " + trait.getCurrentAdoptionCount());
        assertTrue(trait.getCurrentAdoptionCount() == 15);

        log.info("now 2 agents unadopt the trait");
        trait.unadopt(this.agent);
        trait.unadopt(lastAgent);
        log.info("expecting: 13 observed: " + trait.getCurrentAdoptionCount());
        assertTrue(trait.getCurrentAdoptionCount() == 13);
        log.info("Exiting multipleAdoption test");
    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).to(UnstructuredTrait.class);
        binder.bind(IAgent.class).to(AgentFixture.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
        
    }
}
