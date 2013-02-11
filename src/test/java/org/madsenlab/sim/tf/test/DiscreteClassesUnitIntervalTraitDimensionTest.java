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
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.classification.UnitIntervalClassDimension;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.test.util.SingleDimensionAgentModule;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimension;

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
public class DiscreteClassesUnitIntervalTraitDimensionTest extends SingleDimensionAgentModule {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;


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
    public void testRandomModeCreation() throws Exception {
        log.info("entering testRandomModeCreation");

        ITraitDimension<Double> traitDim = new UnstructuredTraitDimension<Double>();
        UnitIntervalClassDimension classDim = new UnitIntervalClassDimension(this.model, traitDim);

        log.info("Create 8 intervals =========================");
        classDim.createRandomModeSet(8);
        log.info("Create 5 intervals =========================");
        classDim.createRandomModeSet(5);
        log.info("Create 2 intervals =========================");
        classDim.createRandomModeSet(2);

        log.warn("THIS TEST DOES NOT ACTUALLY TEST ANYTHING, IT IS BEING USED TO OBSERVE THE RANDOMNESS OF MODE CREATION");

        // TODO:  How to actually test this?  Correct num intervals?
        assertTrue(true);


        log.info("exiting testRandomModeCreation");
    }


}
