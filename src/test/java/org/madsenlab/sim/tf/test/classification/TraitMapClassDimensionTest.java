/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.classification;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.classification.UnitIntervalClassDimension;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.interfaces.ITraitFactory;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimensionMode;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;
import org.madsenlab.sim.tf.traits.InfiniteAllelesUnitIntervalTraitFactory;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimension;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/*
  Let's test the mapping from Traits to TraitDimensions, to a ClassDimension, to observing the correct
  Traits via Modes

  1.  Create a trait dimension that represents [0,1]
  2.  Add some traits to the dimension at specific points along the unit interval.
  3.  Create a ClassDimension that maps this trait dimension
  4.  Create a set of modes that split the ClassDimension into 5 even blocks
  5.  Get the mode list for the ClassDimension
  6.  Evaluate the expected number of traits present for each mode in the ClassDimension

  First Test:

  0.1, 0.15, 0.2,
  0.3, 0.35, 0.4,
  0.55
  0.7, 0.75, 0.76, 0.8,
  0.99999999

  If we split the ClassDimension into 5 even blocks, we should expect to see:

  Mode 0: (0, 0.20] -- 3 traits
  Mode 1: (0.20, 0.40] -- 3 traits
  Mode 2: (0.40, 0.60] -- 1 traits
  Mode 3: (0.60, 0.80] -- 4 traits
  Mode 4: (0.80, 1.0] -- 1 trait

  The second test:

  0.1, 0.15, 0.2,
  0.3, 0.35, 0.4,

  0.7, 0.75, 0.76, 0.8,
  0.99999999

  If we split the ClassDimension into 5 even blocks, we should expect to see:

  Mode 0: (0, 0.20] -- 3 traits
  Mode 1: (0.20, 0.40] -- 3 traits
  Mode 2: (0.40, 0.60] -- 0 traits
  Mode 3: (0.60, 0.80] -- 4 traits
  Mode 4: (0.80, 1.0] -- 1 trait




*/

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class TraitMapClassDimensionTest extends MultidimensionalAgentModule {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;
    private ITraitDimension<Double> traitDimension;
    private ITraitFactory traitFactory;
    private IClassDimension classDim;
    private Map<String, Integer> expectedTraitsPerMode;


    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        // specifically we don't want to see repeatable random numbers here
        model.initializeRNG(false);

        this.expectedTraitsPerMode = new HashMap<String, Integer>();
        this.traitFactory = new InfiniteAllelesUnitIntervalTraitFactory(this.model);
        this.traitDimension = new UnstructuredTraitDimension<Double>(this.model, this.traitFactory);
        this.classDim = new UnitIntervalClassDimension<Double>(this.model, this.traitDimension);


    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
        this.traitDimension = null;
        this.classDim = null;
        this.expectedTraitsPerMode = null;
    }

    private void setupCommonData() {

        // create 5 evenly spaced modes in the ClassDimension
        TraitPredicate pred0 = new RealTraitIntervalPredicate(0.0, true, 0.20, true, "0");
        TraitPredicate pred1 = new RealTraitIntervalPredicate(0.20, false, 0.40, true, "1");
        TraitPredicate pred2 = new RealTraitIntervalPredicate(0.40, false, 0.60, true, "2");
        TraitPredicate pred3 = new RealTraitIntervalPredicate(0.60, false, 0.80, true, "3");
        TraitPredicate pred4 = new RealTraitIntervalPredicate(0.80, false, 1.0, true, "4");

        log.debug(pred0.toString());
        log.debug(pred1.toString());
        log.debug(pred2.toString());
        log.debug(pred3.toString());
        log.debug(pred4.toString());

        Set<TraitPredicate> predSet = new HashSet<TraitPredicate>();
        predSet.add(pred0);
        predSet.add(pred1);
        predSet.add(pred2);
        predSet.add(pred3);
        predSet.add(pred4);

        this.classDim.createSpecifiedModeSet(predSet);

        // create traits common to all tests
        this.traitDimension.getNewVariantWithSpecifiedValue(0.4);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.7);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.75);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.76);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.8);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.999999);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.1);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.15);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.2);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.3);
        this.traitDimension.getNewVariantWithSpecifiedValue(0.35);

        // expected number of traits per mode given the above common traits
        // YOU MUST ALTER ENTRIES APPROPRIATELY IN TESTS THAT ADD OR REMOVE
        // TRAITS FROM THE TRAITDIMENSION
        this.expectedTraitsPerMode.put("0", 3);
        this.expectedTraitsPerMode.put("1", 3);
        this.expectedTraitsPerMode.put("2", 0);
        this.expectedTraitsPerMode.put("3", 4);
        this.expectedTraitsPerMode.put("4", 1);
    }

    private void evaluateTraitsPerMode() {

        log.info("traitDimension trait count: " + traitDimension.getTraitsInDimension().size());
        // now get the mode set, and test numbers of traits per mode
        Set<IClassDimensionMode> modeSet = this.classDim.getModeSet();
        log.info("modeset size: " + modeSet.size());
        assertTrue(modeSet.size() > 0);

        for (IClassDimensionMode mode : modeSet) {
            log.debug("mode: " + mode);
            TraitPredicate pred = mode.getPredicateDefiningMode();
            String modePredicateID = pred.getID();
            log.debug("Mode " + pred.toString());
            Set<ITrait> traitSet = mode.getTraitsMappedToMode(true);
            assertNotNull("modes should not return a null pointer for traitSet", traitSet);

            log.debug("traitSet: " + traitSet);

            Integer modeTraitCount = traitSet.size();
            log.info("Mode " + pred.toString() + " Expected traits: " + this.expectedTraitsPerMode.get(modePredicateID) + " Observed: " + modeTraitCount);
            assertTrue(this.expectedTraitsPerMode.get(modePredicateID).equals(modeTraitCount));
        }
    }

    @Test
    public void testTraitsMapToClassNoEmptyClasses() throws Exception {
        log.info("entering testTraitsMapToClassNoEmptyClasses");

        // Set up the basic test data
        this.setupCommonData();

        // Add data specific to this test and alter the expected answers
        this.traitDimension.getNewVariantWithSpecifiedValue(0.55);
        this.expectedTraitsPerMode.put("2", 1);

        // Evaluate the results
        evaluateTraitsPerMode();


        log.info("exiting testTraitsMapToClassNoEmptyClasses");
    }


    @Test
    public void testTraitsMapToClassWithEmptyClass() throws Exception {
        log.info("entering testTraitsMapToClassWithEmptyClass");
        // Set up the basic test data
        this.setupCommonData();

        // No alterations to data required for this test

        // Evaluate the results
        evaluateTraitsPerMode();

        log.info("exiting testTraitsMapToClassWithEmptyClass");
    }

    /**
     * Set up a ClassDimension, with three modes.
     * Create 9 traits spread across the modes, and create adoption counts for each underlying ITrait.
     * Test whether the class adoption count equals the sum of the trait adoption counts for traits matching each mode.
     *
     * @throws Exception
     */


    @Test
    public void testTotalClassAdoptionCountGivenTraitCounts() throws Exception {
        // TODO:  Needs the rest of IClassification built out beforehand.
    }


}


