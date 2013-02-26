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
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;
import org.madsenlab.sim.tf.traits.InfiniteAllelesIntegerTraitFactory;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimension;

import java.util.Map;

import static org.junit.Assert.assertEquals;
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
public class UnstructuredMultidimensionalTraitAgentTest extends MultidimensionalAgentModule {
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

        ITraitFactory traitFactory = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitFactory traitFactory2 = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitDimension dim1 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory);
        ITraitDimension dim2 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory2);

        // Create two traits in each dimension
        ITrait dim1Trait1 = dim1.getNewUniqueUniformVariant();
        ITrait dim1Trait2 = dim1.getNewUniqueUniformVariant();
        ITrait dim2Trait1 = dim2.getNewUniqueUniformVariant();
        ITrait dim2Trait2 = dim2.getNewUniqueUniformVariant();

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
        Goal of this test is to ensure that we can add a bunch of dimensions and traits, and have
        the correct number when we ask for the "genotype" and its size, particularly in cases
        where we try to add two traits from the same dimension.

     */

    @Test
    public void testAgentGenotypeSize() throws Exception {
        log.info("Entering testAgentGenotypeSize");
        ITraitFactory traitFactory = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitFactory traitFactory2 = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitFactory traitFactory3 = new InfiniteAllelesIntegerTraitFactory(this.model);
        ITraitDimension dim1 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory);
        ITraitDimension dim2 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory2);
        ITraitDimension dim3 = new UnstructuredTraitDimension<Integer>(this.model, traitFactory3);

        IAgent agent = agentProvider.get();

        // Create traits from three dimensions, and two traits in the first dimension
        ITrait dim1Trait1 = dim1.getNewUniqueUniformVariant();
        ITrait dim1Trait2 = dim1.getNewUniqueUniformVariant();
        ITrait dim2Trait1 = dim2.getNewUniqueUniformVariant();
        ITrait dim3Trait1 = dim3.getNewUniqueUniformVariant();

        dim1Trait1.adopt(agent);
        dim2Trait1.adopt(agent);
        dim3Trait1.adopt(agent);

        log.info("Test that adopting 3 orthogonal dimension/traits yields proper genotype size");
        Map<ITraitDimension, ITrait> traitMap = agent.getCurrentlyAdoptedDimensionsAndTraits();
        int numDimensions = traitMap.size();
        log.info("Number of dimensions/traits expected: 3 adopted: " + numDimensions);
        assertTrue(numDimensions == 3);
        numDimensions = 0;

        dim2Trait1.unadopt(agent);

        log.info("Test that unadopting one of the dimension/traits reduces the genotype size");
        Map<ITraitDimension, ITrait> traitMap2 = agent.getCurrentlyAdoptedDimensionsAndTraits();
        int numDimensions2 = traitMap2.size();
        log.info("Number of dimensions/traits expected: 2 adopted: " + numDimensions2);
        assertTrue(numDimensions2 == 2);

        // now adopt the second trait from the first dimension.  This should replace the existing dim1 trait,
        // so the number of dimensions present in an agent should still be two
        dim1Trait2.adopt(agent);

        log.info("Test that adopting a second trait from existing dimension replaces, not adds");
        Map<ITraitDimension, ITrait> traitMap3 = agent.getCurrentlyAdoptedDimensionsAndTraits();
        int numDimensions3 = traitMap3.size();
        log.info("Number of dimensions/traits expected: 2 adopted: " + numDimensions3);
        assertTrue(numDimensions3 == 2);

        log.info("Test that dimension 1 is now represented by trait 2, not trait 1");
        ITrait adoptedTrait = traitMap3.get(dim1);
        assertEquals("Agent did not have dim1trait2 as expected", dim1Trait2, adoptedTrait);

        log.info("Existing testAgentGenotypeSize");
    }

}
