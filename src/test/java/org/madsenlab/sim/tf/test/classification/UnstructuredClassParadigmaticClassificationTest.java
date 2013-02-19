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
import org.madsenlab.sim.tf.classification.UnstructuredClassParadigmaticClassification;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.interfaces.classification.IClass;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassIdentificationEngine;
import org.madsenlab.sim.tf.interfaces.classification.IClassification;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;
import org.madsenlab.sim.tf.traits.InfiniteAllelesUnitIntervalTraitFactory;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimension;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
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
    private Set<IClassDimension> classDimensionSet;
    private IClassification classification;
    private ITraitFactory traitFactory1;
    private ITraitFactory traitFactory2;
    private ITraitDimension dim1;
    private ITraitDimension dim2;


    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        // specifically we don't want to see repeatable random numbers here
        model.initializeRNG(false);
        this.classDimensionSet = new HashSet<IClassDimension>();
        this.setupCommonData();
    }


    @After
    public void tearDown() throws Exception {
        this.classification = null;
        this.classDimensionSet.clear();
    }


    private void setupCommonData() {
        // Set up two predicates which divide the unit interval into halves
        TraitPredicate pred0 = new RealTraitIntervalPredicate(0.0, true, 0.5, true, "Lower");
        TraitPredicate pred1 = new RealTraitIntervalPredicate(0.5, false, 1.0, true, "Upper");

        // Set up two trait dimensions
        this.traitFactory1 = new InfiniteAllelesUnitIntervalTraitFactory(this.model);
        this.traitFactory2 = new InfiniteAllelesUnitIntervalTraitFactory(this.model);
        this.dim1 = new UnstructuredTraitDimension<Double>(this.model, traitFactory1);
        this.dim2 = new UnstructuredTraitDimension<Double>(this.model, traitFactory2);

        // Give the dimensions names for human readable output
        this.dim1.setDimensionName("DIM1");
        this.dim2.setDimensionName("DIM2");

        // Set up class dimensions pointing to each of the trait dimensions, with the 50/50 predicates for modes
        IClassDimension classDim1 = new UnitIntervalClassDimension<Double>(this.model, this.dim1);
        IClassDimension classDim2 = new UnitIntervalClassDimension<Double>(this.model, this.dim2);
        Set<TraitPredicate> predSet = new HashSet<TraitPredicate>();
        predSet.add(pred0);
        predSet.add(pred1);

        classDim1.createSpecifiedModeSet(predSet);
        classDim2.createSpecifiedModeSet(predSet);

        this.classDimensionSet.add(classDim1);
        this.classDimensionSet.add(classDim2);

        // Create a two dimensional classification and intersect to form four classes
        this.classification = new UnstructuredClassParadigmaticClassification(this.model);
        this.classification.addClassDimension(classDim1);
        this.classification.addClassDimension(classDim2);
        this.classification.initializeClasses();

    }


    @Test
    public void testGetNumClasses() throws Exception {
        // Should be four classes given 2 dimensions, 2 modes each
        log.info("entering testGetNumClasses");
        long expected = 4;
        long observed = (long) this.classification.getNumClasses();
        log.info("expected: " + expected + " observed: " + observed);
        log.info("exiting testGetNumClasses");
        assertEquals(expected, observed);
    }

    @Test
    public void testPrintClassDefinitions() throws Exception {
        // Doesn't test anything, just gives a window into the class definitions
        log.info("entering testPrintClassDefinitions -- NOT A TEST");
        Set<IClass> classSet = this.classification.getClasses();
        for (IClass iclass : classSet) {
            log.info("Class definition: " + iclass.getClassDescription());
        }
        log.info("exiting testPrintClassDefinitions -- NOT A TEST");
        assertTrue(true);
    }

    @Test
    public void testGetClassForAgent() throws Exception {
        // Time to test the plumbing!
        log.info("entering testGetClassForAgent");

        // Create an agent
        IAgent agent = this.model.getPopulation().createAgent();

        // Create traits in each of the dimensions, with specified values, agent adopts the trait
        ITrait traitLowDim1 = this.dim1.getNewVariantWithSpecifiedValue(0.25);
        ITrait traitHighDim2 = this.dim2.getNewVariantWithSpecifiedValue(0.90);
        StringBuffer expectedMode1 = new StringBuffer("DIM1:[0.0,0.5]");
        StringBuffer expectedMode2 = new StringBuffer("DIM2:(0.5,1.0]");

        traitLowDim1.adopt(agent);
        traitHighDim2.adopt(agent);

        // Identify the agent's current class
        IClassIdentificationEngine classIdentifier = this.classification.getClassIdentifier();

        IClass agentClass = classIdentifier.getClassForAgent(agent);

        String agentClassDescription = agentClass.getClassDescription();
        Boolean result1 = agentClassDescription.contains(expectedMode1);
        Boolean result2 = agentClassDescription.contains(expectedMode2);

        log.info("expectedMode1 present: " + result1);
        log.info("expectedMode2 present: " + result2);

        assertTrue(result1);
        assertTrue(result2);
        log.info("exiting testGetClassForAgent");
    }

    /**
     * Test that 10 agents adopting the same trait combination yields a class count of 10
     * for the class to which the agents belong
     *
     * @throws Exception
     */

    @Test
    public void testClassAdoptionCounts() throws Exception {
        log.info("entering testClassAdoptionCounts");
        int numAgents = 10;
        Set<IAgent> agentSet = new HashSet<IAgent>();
        for (int i = 0; i < numAgents; i++) {
            agentSet.add(this.model.getPopulation().createAgent());
        }

        ITrait traitLowDim1 = this.dim1.getNewVariantWithSpecifiedValue(0.25);
        ITrait traitHighDim2 = this.dim2.getNewVariantWithSpecifiedValue(0.90);

        for (IAgent agent : agentSet) {
            traitLowDim1.adopt(agent);
            traitHighDim2.adopt(agent);
        }

        // Now update the class counts for the population
        this.classification.updateClassForPopulation(this.model.getPopulation());


        Map<IClass, Integer> countMap = this.classification.getCurGlobalClassCounts();

        // Find the maximum class count.
        int maxClassCount = 0;
        for (Integer count : countMap.values()) {
            log.debug("count: " + count);
            if (count > maxClassCount) {
                maxClassCount = count;
            }
        }

        log.info("Observed highest class count: " + maxClassCount + " expected: " + numAgents);

        log.info("exiting testClassAdoptionCounts");
        assertEquals(maxClassCount, numAgents);
    }

    /*@Test
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
    public void testUpdateClassForAgent() throws Exception {
        assertTrue(false);
    }

    @Test
    public void testUpdateClassForPopulation() throws Exception {
        assertTrue(false);
    }*/
}
