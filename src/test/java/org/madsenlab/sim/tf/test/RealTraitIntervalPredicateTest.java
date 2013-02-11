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
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.test.util.SingleDimensionAgentModule;
import org.madsenlab.sim.tf.traits.UnstructuredTrait;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;

import static org.junit.Assert.assertFalse;
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
public class RealTraitIntervalPredicateTest extends SingleDimensionAgentModule {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;


    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        model.initializeRNG(true);
    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
    }

    @Test
    public void testInsideInterval() throws Exception {
        log.info("entering testInsideInterval");
        RealTraitIntervalPredicate testPredicate = new RealTraitIntervalPredicate(0.0, true, 1.0, true, "Closed-0,1");

        ITrait<Double> testTrait = new UnstructuredTrait<Double>();
        testTrait.setTraitID(0.5);
        boolean result = testPredicate.evaluate(testTrait);
        log.info("expecting: true observed: " + result);
        assertTrue(result);


        log.info("leaving testInsideInterval");
    }

    @Test
    public void testOutsideInterval() throws Exception {
        log.info("entering testOutsideInterval");
        RealTraitIntervalPredicate testPredicate = new RealTraitIntervalPredicate(0.5, true, 1.0, true, "Closed-0.5,1");

        ITrait<Double> testTrait = new UnstructuredTrait<Double>();
        testTrait.setTraitID(0.1);
        boolean result = testPredicate.evaluate(testTrait);

        log.info("expected: false  observed: " + result);
        assertFalse(result);


        log.info("exiting testOutsideInterval");
    }

    @Test
    public void testClosedIntervalBoundary() throws Exception {
        log.info("entering testClosedIntervalBoundary");
        RealTraitIntervalPredicate testPredicate = new RealTraitIntervalPredicate(0.5, true, 1.0, true, "closed-0.5,1");
        ITrait<Double> testTrait = new UnstructuredTrait<Double>();
        testTrait.setTraitID(0.5);

        boolean result = testPredicate.evaluate(testTrait);
        log.info("expected:  true  observed: " + result);

        assertTrue(result);

        log.info("entering testClosedIntervalBoundary");
    }

    @Test
    public void testOpenIntervalBoundary() throws Exception {
        log.info("entering testOpenIntervalBoundary");
        RealTraitIntervalPredicate testPredicate = new RealTraitIntervalPredicate(0.5, false, 1.0, true, "open-0.5,1");
        ITrait<Double> testTrait = new UnstructuredTrait<Double>();
        testTrait.setTraitID(0.5);

        boolean result = testPredicate.evaluate(testTrait);
        log.info("expected:  false  observed: " + result);

        assertFalse(result);

        log.info("entering testOpenIntervalBoundary");
    }

}
