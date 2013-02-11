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
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;

import static org.junit.Assert.assertTrue;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/6/13
 * Time: 10:05 AM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class UnstructuredClassParadigmaticClassificationTest extends MultidimensionalAgentModule {
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
    public void tearDown() throws Exception {
        model.getPopulation().clearAgentPopulation();
    }

    @Test
    public void testGetNumClasses() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testInitializeClasses() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testGetCurGlobalClassCounts() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testGetCurClassCountByTag() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testGetCurGlobalClassFrequencies() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testGetCurClassFreqByTag() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testGetClassForAgent() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testUpdateClassForAgent() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testUpdateClassForPopulation() throws Exception {
        assertTrue(false);
    }
}
