/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
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
import com.google.inject.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.agent.UnstructuredMultidimensionalTraitAgentProvider;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.population.SimpleAgentDemeProvider;
import org.madsenlab.sim.tf.population.SimpleAgentPopulationProvider;
import org.madsenlab.sim.tf.structure.SimpleAgentTagProvider;
import org.madsenlab.sim.tf.structure.WellMixedInteractionTopologyProvider;
import org.madsenlab.sim.tf.test.util.SimulationModelFixture;
import org.madsenlab.sim.tf.traits.InfiniteAllelesIntegerTraitFactory;
import org.madsenlab.sim.tf.traits.IntegerTraitProvider;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimension;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.madsenlab.sim.tf.utils.LogFileHandler;

import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/21/12
 * Time: 10:22 AM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class UnstructuredMultidimensionalTraitAgentTest implements Module {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;
    @Inject
    public Provider<IAgent> agentProvider;
    @Inject
    public Provider<ITrait> traitProvider;
    @Inject
    public Provider<ITraitDimension> dimensionProvider;
    @Inject
    public Provider<IAgentTag> tagProvider;


    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        // specifically we don't want to see repeatable random numbers here
        model.initializeRNG(false);
    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
    }

    @Test
    public void testMultipleTraitAdoption() throws Exception {
        log.info("entering testMultipleTraitAdoption");
        int numAgents = 4;

        ITraitFactory traitFactory = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitFactory traitFactory2 = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitDimension dim1 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory);
        ITraitDimension dim2 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory2);

        // Create two traits in each dimension
        ITrait dim1Trait1 = dim1.getNewVariant();
        ITrait dim1Trait2 = dim1.getNewVariant();
        ITrait dim2Trait1 = dim2.getNewVariant();
        ITrait dim2Trait2 = dim2.getNewVariant();

        IAgent agent1 = agentProvider.get();
        IAgent agent2 = agentProvider.get();

        dim1Trait1.adopt(agent1);
        dim1Trait2.adopt(agent2);
        dim2Trait1.adopt(agent1);
        dim2Trait2.adopt(agent2);

        log.info("Testing two agents adopting two different traits from two dimensions");
        int runningTotal = 0;
        Map<ITrait, Integer> countMap = dim1.getCurGlobalTraitCounts();
        for (Map.Entry<ITrait, Integer> pair : countMap.entrySet()) {
            log.debug("trait: " + pair.getKey() + " count: " + pair.getValue());
            runningTotal += pair.getValue();
            assertTrue(pair.getValue() == 1);
        }

        Map<ITrait, Integer> countMap2 = dim2.getCurGlobalTraitCounts();
        for (Map.Entry<ITrait, Integer> pair : countMap2.entrySet()) {
            log.debug("trait: " + pair.getKey() + " count: " + pair.getValue());
            runningTotal += pair.getValue();
            assertTrue(pair.getValue() == 1);
        }

        log.info("Total number of traits with adoption counts should be 4, is: " + runningTotal);
        assertTrue(runningTotal == 4);

        log.info("exiting testMultipleTraitAdoption");
    }


    /*
        We do not subclass AbstractGuiceTestClass because it configures a unidimensional trait agent
        for the agent provider.

        TODO:  refactor the way tests are configured to allow multiple options for a given interface easily
     */
    public void configure(Binder binder) {

        binder.bind(ISimulationModel.class)
                .to(SimulationModelFixture.class)
                .in(Singleton.class);
        binder.bind(IAgent.class)
                .toProvider(UnstructuredMultidimensionalTraitAgentProvider.class);
        binder.bind(ITraitDimension.class)
                .toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(ITrait.class)
                .toProvider(IntegerTraitProvider.class);
        binder.bind(IAgentTag.class)
                .toProvider(SimpleAgentTagProvider.class);
        binder.bind(IPopulation.class).toProvider(SimpleAgentPopulationProvider.class);
        binder.bind(IInteractionTopology.class).toProvider(WellMixedInteractionTopologyProvider.class);
        binder.bind(IDeme.class).toProvider(SimpleAgentDemeProvider.class);
        binder.bind(ILogFiles.class).to(LogFileHandler.class).in(Singleton.class);
    }

}
