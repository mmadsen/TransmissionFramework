/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
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
import com.google.inject.Module;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.test.util.AbstractGuiceTestClass;
import org.madsenlab.sim.tf.utils.AgentTagPredicate;
import org.madsenlab.sim.tf.utils.AllAgentsPredicate;

import static org.junit.Assert.assertTrue;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 30, 2010
 * Time: 3:26:55 PM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class PopulationDemeTest extends AbstractGuiceTestClass implements Module {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;
    @Inject
    public Provider<IAgentTag> tagProvider;
    private IAgentTag redTag;
    private IAgentTag blueTag;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        model.initializeRNG(true);
    }

    @Test
    public void testTagBasedDemeQuery() throws Exception {
        log.info("Entering testTagBasedDemeQuery");
        IAgentTag redTag = tagProvider.get();
        redTag.setTagName("redTag");
        IAgentTag blueTag = tagProvider.get();
        blueTag.setTagName("blueTag");

        this._createTestData(redTag, blueTag);

        IPopulation pop = this.model.getPopulation();


        IDeme redDeme = pop.getDemeForTag(redTag);
        log.info("redDeme population: " + redDeme.getCurrentPopulationSize());


        assertTrue(redDeme.getCurrentPopulationSize() == 10);
        log.info("Exiting testTagBasedDemeQuery");
    }

    @Test
    public void testTagQueryCustomPredicate() throws Exception {
        log.info("Entering testTagQueryCustomPredicate");
        IAgentTag redTag = tagProvider.get();
        redTag.setTagName("redTag");
        IAgentTag blueTag = tagProvider.get();
        blueTag.setTagName("blueTag");

        this._createTestData(redTag, blueTag);

        IPopulation pop = this.model.getPopulation();


        AgentTagPredicate bluePred = new AgentTagPredicate(blueTag);
        IDeme blueDeme = pop.getDemeMatchingPredicate(bluePred);

        log.info("blueDeme population: " + blueDeme.getCurrentPopulationSize());

        assertTrue(blueDeme.getCurrentPopulationSize() == 5);
        log.info("Exiting testTagQueryCustomPredicate");

    }

    @Test
    public void testAllAgentPredicate() throws Exception {
        log.info("Entering testAllAgentPredicate");
        // Not needed here, but keeps me from having to redo the test data creation, we just care
        // about a count here
        IAgentTag redTag = tagProvider.get();
        redTag.setTagName("redTag");
        IAgentTag blueTag = tagProvider.get();
        blueTag.setTagName("blueTag");

        this._createTestData(redTag, blueTag);
        IPopulation pop = this.model.getPopulation();

        int popsize = pop.getCurrentPopulationSize();

        IDeme deme = pop.getDemeMatchingPredicate(new AllAgentsPredicate());

        int predicatedSize = deme.getCurrentPopulationSize();

        assertTrue(popsize == predicatedSize);

        log.info("PASS:  AllAgentsPredicate returns full set of agents");

    }


    private void _createTestData(IAgentTag redTag, IAgentTag blueTag) {
        log.info("Entering _createTestData");

        log.info("Adding 10 agents carrying a red tag");
        // add 10 agents carrying the red tag to the population
        for (int i = 0; i < 10; i++) {
            IAgent agent = this.model.getPopulation().createAgent();
            agent.addTag(redTag);
        }

        log.info("Adding 5 agents carrying a blue tag");
        // add 5 agents carrying the blue tag to the population
        for (int i = 0; i < 5; i++) {
            IAgent agent = this.model.getPopulation().createAgent();
            agent.addTag(blueTag);
        }
        log.info("Exiting _createTestData");
    }

}
