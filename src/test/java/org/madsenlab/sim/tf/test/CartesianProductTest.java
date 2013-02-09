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
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.test.util.AbstractGuiceTestClass;

import java.util.List;
import java.util.Set;

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
public class CartesianProductTest extends AbstractGuiceTestClass implements Module {
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
    public void testCartesianProduct() throws Exception {
        Set<String>[] dimensionModeSets = new Set[2];
        dimensionModeSets[0] = ImmutableSet.of("A", "B", "C");
        dimensionModeSets[1] = ImmutableSet.of("1", "2", "3");

        Set<List<String>> setAllClasses = Sets.cartesianProduct(dimensionModeSets);
        for (List<String> list : setAllClasses) {
            log.info(list);
        }
        assertTrue(setAllClasses.size() == 9);
    }


}
