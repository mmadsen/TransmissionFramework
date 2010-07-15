/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.test;

import static org.junit.Assert.*;
import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.common.base.Preconditions;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mmadsen.sim.tf.agent.SimpleAgentProvider;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * Tests for various simulation-relevant aspects of population dynamics -
 * i.e., adding agents, removing them from the population, and keeping accurate
 * population size numbers.
 *
 * User: mark
 * Date: Jul 12, 2010
 * Time: 9:51:51 AM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class PopulationDynamicsManagementTest implements Module {
    @Inject @Unit public ISimulationModel model;
    Logger log;

    @Before
    public void setUp(){
        log = model.getModelLogger(this.getClass());

    }

    @Test
    public void testAgentCreationAndTracking() throws Exception {
        log.info("Entering testAgentCreationAndTracking");
        Integer desiredAgentCount = 22;
        List<IAgent> agentList = new ArrayList<IAgent>();

        for(Integer i = 0; i < desiredAgentCount; i++) {
            IAgent newAgent = model.createAgent();
            agentList.add(newAgent);
        }
        log.info("expected population size: " + desiredAgentCount + " observed: " + model.getCurrentPopulationSize());
        assertTrue(model.getCurrentPopulationSize() == desiredAgentCount);

        IAgent agentToDelete = agentList.get(0);
        model.removeAgent(agentToDelete);

        log.info("expected population size: " + (desiredAgentCount - 1) + " observed: " + model.getCurrentPopulationSize());
        assertTrue(model.getCurrentPopulationSize() == (desiredAgentCount - 1));

        log.info("Exiting testAgentCreationAndTracking");
    }


    @Test
    public void testMultithreadedAgentManagement() {
        List<PopThread> tList = new ArrayList<PopThread>();
        log.info("Entering testMultithreadedAgentManagement");
        Integer threadCount = 20;
        Integer agentsPerThread = 10;
        for(Integer i = 0; i < threadCount; i++) {
            PopThread pt = new PopThread(i.toString(), agentsPerThread);
            tList.add(pt);
        }

        // now stop the main test thread to wait for the others to finish
        for(PopThread pt: tList) {
            try {
                pt.join();
            }catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Integer expected = threadCount * agentsPerThread;
        log.info("expected agents: " + expected + " observed: " + model.getCurrentPopulationSize());
        assertTrue(model.getCurrentPopulationSize().equals(expected));

        log.info("Entering testMultithreadedAgentManagement");
    }

    



    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(ITraitDimension.class).toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(AgentFixtureProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
    }

    private class PopThread extends Thread {
        private Integer agentsToCreate;

        public PopThread(String name, Integer agentCount) {
            super(name);
            agentsToCreate = agentCount;
            start();
        }

        public void run() {
            while(agentsToCreate > 0) {
                try {
                    Thread.sleep(Math.round(Math.random() * 3));
                } catch(InterruptedException e) {
                    throw new RuntimeException(e);
                }
                IAgent agent = model.createAgent();
                agentsToCreate--;
            }
            return;
        }
    }


}
