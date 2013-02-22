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
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.classification.UnitIntervalClassDimension;
import org.madsenlab.sim.tf.classification.UnstructuredClassParadigmaticClassification;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassification;
import org.madsenlab.sim.tf.test.util.ClassificationDummyObserver;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;
import org.madsenlab.sim.tf.traits.InfiniteAllelesUnitIntervalTraitFactory;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimension;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.util.*;

import static org.junit.Assert.assertEquals;


/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 1, 2010
 * Time: 2:21:51 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class UnstructuredParadigmaticClassificationObserverTest extends MultidimensionalAgentModule {
    @Inject
    public Provider<IAgent> agentProvider;
    @Inject
    public Provider<ITrait> traitProvider;
    @Inject
    public Provider<ITraitDimension> dimensionProvider;
    @Inject
    public Provider<IAgentTag> tagProvider;
    @Inject
    public
    ISimulationModel model;
    @Unit
    ITraitDimension unusedDim;  // this makes Junit4 happy
    Logger log;


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
        this.setupClassification();

    }

    @After
    public void cleanUp() throws Exception {
        model.initializeProviders();
        this.classification = null;
        this.classDimensionSet.clear();
    }

    private void setupClassification() {
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
    public void testObserverBasedClassCounting() throws Exception {
        log.info("entering testObserverBasedClassCounting");
        ClassificationDummyObserver obs = new ClassificationDummyObserver(this.model);
        this.classification.attach(obs);

        List<Integer> expectedCountList = new ArrayList<Integer>();
        expectedCountList.add(0);
        expectedCountList.add(0);
        expectedCountList.add(0);
        expectedCountList.add(10);

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
        this.classification.notifyObservers();


        List<Integer> observedCountList = obs.getAdoptionEventCount();
        Collections.sort(observedCountList);

        log.info("Observed: " + observedCountList.toString() + " expected: " + expectedCountList.toString());

        assertEquals(observedCountList, expectedCountList);

        log.info("exiting testObserverBasedClassCounting");
    }


}
