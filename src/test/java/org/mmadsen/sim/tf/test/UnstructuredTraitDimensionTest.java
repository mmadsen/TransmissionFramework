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
import com.google.inject.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mmadsen.sim.tf.agent.UnstructuredTraitAgentProvider;
import org.mmadsen.sim.tf.interfaces.*;
import org.mmadsen.sim.tf.population.SimpleAgentDemeProvider;
import org.mmadsen.sim.tf.population.SimpleAgentPopulationProvider;
import org.mmadsen.sim.tf.structure.SimpleAgentTagProvider;
import org.mmadsen.sim.tf.test.util.SimulationModelFixture;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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
    @Inject public Provider<IAgentTag> tagProvider;
    @Inject public
    ISimulationModel model;
    @Unit ITraitDimension unusedDim;  // this makes Junit4 happy
    Logger log;

    ITrait three, five;
    IAgentTag redTag, blueTag;
    Integer expectedThreeRed, expectedFiveBlue;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializePopulation();

        log.info("initializing model: " + model + " population: " + model.getPopulation());

    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
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
        ITraitDimension dimension = this._createTestData();

        Map<ITrait,Integer> countMap = dimension.getCurGlobalTraitCounts();
        // do we have 5 tuples in the map?
        assertTrue(countMap.size() == 5);

        // check a couple of entries
        assertTrue(countMap.get(three) == 9);
        log.info("expected: 9 observed: " + countMap.get(three));
        assertTrue(countMap.get(five) == 15);
        log.info("expected: 15 observed: " + countMap.get(five));
        
        log.info("Exiting testGetCurrentTraitCountMap");
    }

    @Test
    public void testGetCurrentTraitFreqMap() throws Exception {
        log.info("Entering testGetCurrentTraitFreqMap");
        ITraitDimension dimension = this._createTestData();

        Map<ITrait,Double> freqMap = dimension.getCurGlobalTraitFrequencies();
        // do we have 5 tuples in the map?
        assertTrue(freqMap.size() == 5);

        for(ITrait aTrait: freqMap.keySet()) {
            log.info("trait: " + aTrait.getTraitID() + " freq: " + freqMap.get(aTrait));
        }

        // check a couple of entries
        double target1 = (double) 15 / (double) 45;
        double target2 = (double) 9 / (double) 45;
        log.info("expected: " + target1 + " observed: " + freqMap.get(five));
        assertTrue(freqMap.get(five) == target1);
        log.info("expected: " + target2 + " observed: " + freqMap.get(three));
        assertTrue(freqMap.get(three) == target2);


        log.info("Exiting testGetCurrentTraitFreqMap");
    }



    @Test
    public void testTagBasedStatistics() throws Exception {
        log.info("entering testTagBasedStatistics");

        
        ITraitDimension dim = this._createTestData();


        // Test adoption counts by tag

        Map<ITrait,Integer> redCounts = dim.getCurTraitCountByTag(redTag);
        Map<ITrait,Integer> blueCounts = dim.getCurTraitCountByTag(blueTag);

        Integer obsThreeRed = redCounts.get(three);
        Integer obsFiveBlue = blueCounts.get(five);

        log.info("expected three red count: " + expectedThreeRed + " observed: " + obsThreeRed );
        log.info("expected five blue count: " + expectedFiveBlue + " observed: " + obsFiveBlue );

        assertTrue(obsThreeRed.equals(expectedThreeRed));
        assertTrue(obsFiveBlue.equals(expectedFiveBlue));

        // Now test frequency counts by tag

        Map<ITrait,Double> redFreq = dim.getCurTraitFreqByTag(redTag);
        Map<ITrait,Double> blueFreq = dim.getCurTraitFreqByTag(blueTag);

        Integer redTagCount = redTag.curAgentCount();
        Integer blueTagCount = blueTag.curAgentCount();

        Double expectedThreeRedFreq = (double) expectedThreeRed / (double) redTagCount;
        Double expectedFiveBlueFreq = (double) expectedFiveBlue / (double) blueTagCount;

        Double obsThreeRedFreq = redFreq.get(three);
        Double obsFiveBlueFreq = blueFreq.get(five);

        log.info("expected three red freq: " + expectedThreeRedFreq + " observed: " + obsThreeRedFreq );
        log.info("expected five blue freq: " + expectedFiveBlueFreq + " observed: " + obsFiveBlueFreq );

        assertEquals(expectedThreeRedFreq,obsThreeRedFreq);
        assertEquals(expectedFiveBlueFreq,obsFiveBlueFreq);

        log.info("exiting testTagBasedStatistics");
    }


    private ITraitDimension _createTestData() {
        log.info("entering _createTestData");
        ITraitDimension dimension = dimensionProvider.get();
        this.redTag = tagProvider.get();
        redTag.setTagName("redTag");
        this.blueTag = tagProvider.get();
        blueTag.setTagName("blueTag");

        // We're going to add five traits to a dimension
        for(Integer i = 1; i < 6; i++) {
            // we can't rely on injection here, so just construct them directly.
            ITrait newTrait = traitProvider.get();
            newTrait.setOwningDimension(dimension);
            newTrait.setTraitID(i.toString());

            // stash references to a couple of traits to use in tests later
            if (i == 3) { three = newTrait; }
            if (i == 5) { five = newTrait; }

            // Now let 3i agents adopt each trait
            // This means first trait is adopted three times, second trait is adopted 6 times,
            // This will give us an easy way to check accuracy of frequencies, etc.

            // And we do this with a 2:1 ratio of red to blue tags, to test the tag-based methods
            for(Integer j = 1; j <= 2 * i; j++) {
                // calculate expected value for trait 3 , red tag
                if(i == 3) {
                    expectedThreeRed = i * 2;
                }



                IAgent newAgent = this.model.getPopulation().createAgent();
                
                redTag.registerAgent(newAgent);
                newTrait.adopt(newAgent);
            }

            for(Integer j = 1; j <= i; j++) {
                // calculated expected value for trait 5, blue tag
                if(i == 5) {
                    expectedFiveBlue = i;
                }

                IAgent newAgent = this.model.getPopulation().createAgent();
                blueTag.registerAgent(newAgent);
                newTrait.adopt(newAgent);
            }

            dimension.addTrait(newTrait);

        }

        log.info("_createTestData: initialized " + this.model.getPopulation().getCurrentPopulationSize() + " agents");

        log.info("exiting _createTestData");
        return dimension;
    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(ITraitDimension.class).toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(UnstructuredTraitAgentProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class).in(Singleton.class);
        binder.bind(IAgentTag.class).toProvider(SimpleAgentTagProvider.class);
        binder.bind(IPopulation.class).toProvider(SimpleAgentPopulationProvider.class);
        binder.bind(IDeme.class).toProvider(SimpleAgentDemeProvider.class);
    }
}
