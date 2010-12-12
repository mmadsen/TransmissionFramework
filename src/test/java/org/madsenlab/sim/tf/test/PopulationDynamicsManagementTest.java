/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.agent.UnstructuredTraitAgentProvider;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.population.SimpleAgentDemeProvider;
import org.madsenlab.sim.tf.population.SimpleAgentPopulationProvider;
import org.madsenlab.sim.tf.structure.WellMixedInteractionTopologyProvider;
import org.madsenlab.sim.tf.test.util.SimulationModelFixture;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.madsenlab.sim.tf.traits.UnstructuredTraitProvider;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * Tests for various simulation-relevant aspects of population dynamics -
 * i.e., adding agents, removing them from the population, and keeping accurate
 * population size numbers.
 * <p/>
 * User: mark
 * Date: Jul 12, 2010
 * Time: 9:51:51 AM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class PopulationDynamicsManagementTest implements Module {
    @Inject
    @Unit
    public ISimulationModel model;
    Logger log;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializePopulation();
    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
    }

    @Test
    public void testAgentCreationAndTracking() throws Exception {
        log.info("Entering testAgentCreationAndTracking");
        Integer desiredAgentCount = 22;
        List<IAgent> agentList = new ArrayList<IAgent>();

        for (Integer i = 0; i < desiredAgentCount; i++) {
            IAgent newAgent = model.getPopulation().createAgent();
            agentList.add(newAgent);
        }
        log.info("expected population size: " + desiredAgentCount + " observed: " + model.getPopulation().getCurrentPopulationSize());
        assertTrue(model.getPopulation().getCurrentPopulationSize() == desiredAgentCount);

        IAgent agentToDelete = agentList.get(0);
        model.getPopulation().removeAgent(agentToDelete);

        log.info("expected population size: " + (desiredAgentCount - 1) + " observed: " + model.getPopulation().getCurrentPopulationSize());
        assertTrue(model.getPopulation().getCurrentPopulationSize() == (desiredAgentCount - 1));

        log.info("Exiting testAgentCreationAndTracking");
    }


    @Test
    public void testMultithreadedAgentManagement() {
        List<PopThread> tList = new ArrayList<PopThread>();
        log.info("Entering testMultithreadedAgentManagement");
        Integer threadCount = 20;
        Integer agentsPerThread = 10;
        for (Integer i = 0; i < threadCount; i++) {
            PopThread pt = new PopThread(i.toString(), agentsPerThread);
            tList.add(pt);
        }

        // now stop the main test thread to wait for the others to finish
        for (PopThread pt : tList) {
            try {
                pt.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Integer expected = threadCount * agentsPerThread;
        log.info("expected agents: " + expected + " observed: " + model.getPopulation().getCurrentPopulationSize());
        assertTrue(model.getPopulation().getCurrentPopulationSize().equals(expected));

        log.info("Entering testMultithreadedAgentManagement");
    }


    private class PopThread extends Thread {
        private Integer agentsToCreate;

        public PopThread(String name, Integer agentCount) {
            super(name);
            agentsToCreate = agentCount;
            start();
        }

        public void run() {
            while (agentsToCreate > 0) {
                try {
                    Thread.sleep(Math.round(Math.random() * 3));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                IAgent agent = model.getPopulation().createAgent();
                agentsToCreate--;
            }
            return;
        }
    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(ITraitDimension.class).toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(UnstructuredTraitAgentProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class).in(Singleton.class);
        binder.bind(IPopulation.class).toProvider(SimpleAgentPopulationProvider.class);
        binder.bind(IDeme.class).toProvider(SimpleAgentDemeProvider.class);
        binder.bind(IInteractionTopology.class).toProvider(WellMixedInteractionTopologyProvider.class);
    }
}
