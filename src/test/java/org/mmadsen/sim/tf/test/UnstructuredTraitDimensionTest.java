package org.mmadsen.sim.tf.test;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import com.google.inject.Inject;
import org.apache.log4j.Logger;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;
import org.mmadsen.sim.tf.traits.UnstructuredTrait;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimension;

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
    @Inject
    ITrait trait;
    @Inject
    IAgent agent;
    @Inject
    ISimulationModel model;
    @Inject @Unit
    ITraitDimension dimension;
    Logger log;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
    }

    @After
    public void tearDown() throws Exception {
        // probably unnecessary, but clean up between tests
        dimension = null;
        agent = null;
        trait = null;
    }

    @Test
    public void testAddTraitAndGetTraitsInDimension() throws Exception {
        log.info("Entering testAddTraitAndGetTraitsInDimension");
        dimension.addTrait(trait);
        Collection<ITrait> traitList = dimension.getTraitsInDimension();
        log.info("expected size: 1 observed: " + traitList.size());
        assertTrue(traitList.size() == 1);

        log.info("Exiting testAddTraitAndGetTraitsInDimension");
    }

    @Test
    public void testGetCurrentTraitCountMap() throws Exception {
        log.info("Entering testGetCurrentTraitCountMap");
        this._createTestData(dimension, model);

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
        this._createTestData(dimension, model);

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


    private void _createTestData(ITraitDimension dimension, ISimulationModel model) {
        log.info("entering _createTestData");
        // We're going to add five traits to a dimension
        for(Integer i = 1; i < 6; i++) {
            // we can't rely on injection here, so just construct them directly.
            UnstructuredTrait newTrait = new UnstructuredTrait();
            newTrait.setOwningDimension(dimension);
            newTrait.setSimulationModel(model);
            newTrait.setTraitID(i.toString());
            // Now let 3i agents adopt each trait
            // This means first trait is adopted three times, second trait is adopted 6 times,
            // This will give us an easy way to check accuracy of frequencies, etc.
            for(Integer j = 1; j <= 3 * i; j++) {
                AgentFixture newAgent = new AgentFixture();
                newAgent.setSimulationModel(model);
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
        binder.bind(ITrait.class).to(UnstructuredTrait.class);
        binder.bind(IAgent.class).to(AgentFixture.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
        binder.bind(ITraitDimension.class).to(UnstructuredTraitDimension.class);
    }
}
