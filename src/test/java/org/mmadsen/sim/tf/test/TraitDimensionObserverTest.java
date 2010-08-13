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
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mmadsen.sim.tf.agent.UnstructuredTraitAgentProvider;
import org.mmadsen.sim.tf.interfaces.*;
import org.mmadsen.sim.tf.structure.SimpleAgentTagProvider;
import org.mmadsen.sim.tf.test.util.SimulationModelFixture;
import org.mmadsen.sim.tf.test.util.TestObserver;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

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
public class TraitDimensionObserverTest implements Module {
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
        model.clearAgentPopulation();

    }

    @After
    public void cleanUp() throws Exception {
        model.clearAgentPopulation();
    }




    @Test
    public void testObserverBasedTraitCounting() throws Exception {
        log.info("entering testObserverBasedTraitCounting");
        TestObserver obs = new TestObserver(model);

        ITraitDimension dimension = dimensionProvider.get();
        this.redTag = tagProvider.get();
        redTag.setTagName("redTag");
        this.blueTag = tagProvider.get();
        blueTag.setTagName("blueTag");


        // We're going to add eight traits to a dimension
        for(Integer i = 2; i < 10; i++) {
            // we can't rely on injection here, so just construct them directly.
            ITrait newTrait = traitProvider.get();

            newTrait.setOwningDimension(dimension);
            newTrait.setTraitID(i.toString());
            dimension.addTrait(newTrait);
            newTrait.attach(obs);
            //log.info("creating trait " + i );


            for(Integer j = 0; j < (i*2); j++) {
                IAgent newAgent = model.createAgent();
                StringBuffer sb = new StringBuffer();
                sb.append("<"+i+"."+j+">");
                newAgent.setAgentID(sb.toString());
                redTag.registerAgent(newAgent);
                newTrait.adopt(newAgent);
            }



        }

        
        assertTrue(true);

    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(ITraitDimension.class).toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(UnstructuredTraitAgentProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
        binder.bind(IAgentTag.class).toProvider(SimpleAgentTagProvider.class);
    }
}