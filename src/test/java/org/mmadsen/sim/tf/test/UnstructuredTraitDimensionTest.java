/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.test;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.mmadsen.sim.tf.agent.SimpleAgentProvider;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;
import org.mmadsen.sim.tf.traits.UnstructuredTrait;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimension;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

import java.util.Collection;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 1, 2010
 * Time: 2:21:51 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class UnstructuredTraitDimensionTest implements Module {
    @Inject public Provider<IAgent> agentProvider;
    @Inject public Provider<ITrait> traitProvider;
    @Inject public Provider<ITraitDimension> dimensionProvider;
    @Inject public
    ISimulationModel model;
    @Unit ITraitDimension unusedDim;  // this makes Junit4 happy
    Logger log;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
    }

    @Test
    public void testAddTraitAndGetTraitsInDimension() throws Exception {

        log.info("Entering testAddTraitAndGetTraitsInDimension");
        ITraitDimension dimension = dimensionProvider.get();
        ITrait trait = traitProvider.get();
        dimension.addTrait(trait);
        Collection<ITrait> traitList = dimension.getTraitsInDimension();
        log.info("expected size: 1 observed: " + traitList.size());
        assertTrue(traitList.size() == 1);

        log.info("Exiting testAddTraitAndGetTraitsInDimension");
    }

    @Test
    public void testGetCurrentTraitCountMap() throws Exception {
        log.info("Entering testGetCurrentTraitCountMap");
        ITraitDimension dimension = dimensionProvider.get();
        this._createTestData(dimension);

        Map<String,Integer> countMap = dimension.getCurGlobalTraitCounts();
        // do we have 5 tuples in the map?
        assertTrue(countMap.size() == 5);

        // check a couple of entries
        assertTrue(countMap.get("3") == 9);
        log.info("expected: 9 observed: " + countMap.get("3"));
        assertTrue(countMap.get("5") == 15);
        log.info("expected: 15 observed: " + countMap.get("5"));
        
        log.info("Exiting testGetCurrentTraitCountMap");
    }

    @Test
    public void testGetCurrentTraitFreqMap() throws Exception {
        log.info("Entering testGetCurrentTraitFreqMap");
        ITraitDimension dimension = dimensionProvider.get();
        this._createTestData(dimension);

        Map<String,Double> freqMap = dimension.getCurGlobalTraitFrequencies();
        // do we have 5 tuples in the map?
        assertTrue(freqMap.size() == 5);

        // check a couple of entries
        double target1 = (double) 15 / (double) 45;
        double target2 = (double) 9 / (double) 45;
        assertTrue(freqMap.get("5") == target1);
        log.info("expected: " + target1 + " observed: " + freqMap.get("5"));
        assertTrue(freqMap.get("3") == target2);
        log.info("expected: " + target2 + " observed: " + freqMap.get("3"));

        log.info("Exiting testGetCurrentTraitFreqMap");
    }

//    @Test
//    public void testRemoveTrait() throws Exception {
//        log.info("Entering testRemoveTrait");
//
//
//        assertTrue(false);
//        log.info("Exiting testRemoveTrait");
//    }


    private void _createTestData(ITraitDimension dimension) {
        log.info("entering _createTestData");
        // We're going to add five traits to a dimension
        for(Integer i = 1; i < 6; i++) {
            // we can't rely on injection here, so just construct them directly.
            ITrait newTrait = traitProvider.get();
            newTrait.setOwningDimension(dimension);
            newTrait.setTraitID(i.toString());
            // Now let 3i agents adopt each trait
            // This means first trait is adopted three times, second trait is adopted 6 times,
            // This will give us an easy way to check accuracy of frequencies, etc.
            for(Integer j = 1; j <= 3 * i; j++) {
                IAgent newAgent = agentProvider.get();
                newTrait.adopt(newAgent);
            }

            dimension.addTrait(newTrait);

        }

        // tell the model we currently have 45 agents in the population
        // NOT a production use of the ISimulationModel interface!
        ((SimulationModelFixture)model).testSetCurrentPopulationSize(45);

        log.info("exiting _createTestData");
    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(ITraitDimension.class).toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(SimpleAgentProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
    }
}
