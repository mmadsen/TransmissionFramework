/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.configuration;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.config.*;
import org.madsenlab.sim.tf.interfaces.IModelDynamics;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
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
public class SimulationConfigurationFactoryTest extends MultidimensionalAgentModule {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;
    private ModelConfiguration mc;


    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        // specifically we don't want to see repeatable random numbers here
        model.initializeRNG(false);

        SimulationConfigurationFactory configFactory = new SimulationConfigurationFactory(this.model);
        configFactory.processConfigurationFile("src/test/config/model-configuration-test.xml");

        this.mc = configFactory.getModelConfiguration();
    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
        this.mc = null;
    }

    @Test
    public void testFoo() throws Exception {

    }

    @Test
    public void testSimlength() throws Exception {
        log.info("entering testSimlength");
        int expected = 10000;
        int observed = this.mc.getSimlength();
        assertEquals(expected, observed);
    }

    @Test
    public void testPopulationNumAgents() throws Exception {
        log.info("entering testPopulationNumAgents");
        int expected = 2000;
        int observed = this.mc.getPopulation().getNumagents();
        assertEquals(expected, observed);
    }

    @Test
    public void testNumRulesets() throws Exception {
        log.info("entering testNumRulesets");
        int expected = 2;
        int observed = this.mc.getPopulation().getRulesets().size();

        assertEquals(expected, observed);
    }

    @Test
    public void testCorrectNumberRules() throws Exception {
        log.info("entering testCorrectNumberRules");
        List<Integer> expectedList = new ArrayList<Integer>();
        List<Integer> observedList = new ArrayList<Integer>();
        expectedList.add(3);
        expectedList.add(5);

        List<RulesetConfiguration> rulesetList = this.mc.getPopulation().getRulesets();
        for (RulesetConfiguration ruleset : rulesetList) {
            int rulesetSize = ruleset.getRuleList().size();
            observedList.add(rulesetSize);

        }

        // for the two lists to be identical in their elements, the observed should not change
        // as the result of this call, and we will get a FALSE
        Boolean incompleteOverlap = observedList.retainAll(expectedList);
        assertFalse("One or more observed values for ruleset size were different than expected", incompleteOverlap);
    }

    @Test
    public void testTraitDimensionParameter() throws Exception {
        log.info("entering testTraitDimensionParameter");
        // get the trait dimension corresponding to ID #1, verify that its variation
        // model has a numtraits parameter of 100
        int expected = 100;
        int observed = 0;

        List<TraitDimensionConfiguration> tdcList = this.mc.getTraitDimensionConfigurations();
        for (TraitDimensionConfiguration tdc : tdcList) {
            if (tdc.getDimensionID() == 1) {
                observed = Integer.parseInt(tdc.getInitialTraitGeneratorParameter("numtraits"));
            }
        }
        assertEquals(expected, observed);
    }

    @Test
    public void testCorrectNumObservers() throws Exception {
        log.info("entering testCorrectNumObservers");
        int expected = 6;
        int observed = this.mc.getObserverConfigurations().size();
        assertEquals(expected, observed);
    }

    @Test
    public void testObserverParameters() throws Exception {
        log.info("entering testObserverParameters");
        String expectedSSString = "100";
        String observedSSString = null;
        List<ObserverConfiguration> obsconfigList = this.mc.getObserverConfigurations();
        for (ObserverConfiguration obsConfig : obsconfigList) {
            if (obsConfig.getObserverClass().contains("Ewens")) {
                observedSSString = obsConfig.getParameterMap().get("samplesize");
            }
        }
        assertEquals("Did not get the expected sample size from the Ewens observer", expectedSSString, observedSSString);
    }


    @Test
    public void testNumClassificationDimension() throws Exception {
        log.info("entering testClassificationDimension");
        int expected = 2;
        List<ClassificationConfiguration> ccList = this.mc.getClassificationConfigurations();
        ClassificationConfiguration cc = ccList.get(0);
        int observed = cc.getClassificationDimensionConfigurations().size();
        assertEquals(expected, observed);
    }

    @Test
    public void testInstantiationDynamicsClass() throws Exception {
        log.info("entering testInstantiationDynamicsClass");
        String dynamicsDelegate = this.mc.getDynamicsclass();
        IModelDynamics mdd = null;
        try {
            Class<?> clazz = Class.forName(dynamicsDelegate);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            mdd = (IModelDynamics) constructor.newInstance(this.model);
        } catch (Exception ex) {
            assertTrue(ex.getMessage(), false);
        }

        log.info("instantiated dynamics class: " + mdd.getClass().getSimpleName());
        assertTrue(true);
    }
}
