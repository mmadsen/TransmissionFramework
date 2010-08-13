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
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimension;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 17, 2010
 * Time: 4:09:58 PM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class TagCountingTest implements Module {
    @Unit
    private IAgentTag unusedTag; // here to make Junit4 happy
    @Inject private ISimulationModel model;
    @Inject private Provider<IAgent> agentProvider;
    @Inject private Provider<IAgentTag> tagProvider;
    @Inject private Provider<ITrait> traitProvider;
    private Logger log;

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
    public void testCountingByTag() {
        List<IAgent> agentListBlueTag = new ArrayList<IAgent>();
        List<IAgent> agentListGreenTag = new ArrayList<IAgent>();
        IAgentTag blueTag = tagProvider.get();
        IAgentTag greenTag = tagProvider.get();
        blueTag.setTagName("blueTag");
        greenTag.setTagName("greenTag");
        ITrait trait = traitProvider.get();

        Integer numBlue = 10;
        Integer numGreen = 20;
        Integer numBoth = 20;

        // Create a bunch of agents, with 10 having only blue, 20 having only green,
        // and 20 having both tags.  Thus, the population should have 30 blue tags,
        // and 40 green tags, out of 50 total agents.

        for(Integer i = 0; i < numBlue; i++) {
            IAgent agent = agentProvider.get();
            agent.addTag(blueTag);
            trait.adopt(agent);
            agentListBlueTag.add(agent);
        }

        for(Integer i = 0; i < numGreen; i++) {
            IAgent agent = agentProvider.get();
            agent.addTag(greenTag);
            trait.adopt(agent);
            agentListGreenTag.add(agent);
        }

        IAgent agentBothForTest = agentProvider.get();
        agentBothForTest.addTag(greenTag);
        agentBothForTest.addTag(blueTag);
        trait.adopt(agentBothForTest);
        agentListGreenTag.add(agentBothForTest);
        agentListBlueTag.add(agentBothForTest);

        for(Integer i = 0; i < (numBoth - 1); i++) {
            IAgent agent = agentProvider.get();
            agent.addTag(greenTag);
            agent.addTag(blueTag);
            trait.adopt(agent);
            agentListGreenTag.add(agent);
            agentListBlueTag.add(agent);
        }


        // check initial results
        Integer expectedBlue = 30;
        Integer expectedGreen = 40;
        Integer totalAgents = 50;


        Integer adoptionCount = trait.getCurrentAdoptionCount();
        log.info("expected adopting agents: " + totalAgents + " observed: " + adoptionCount);
        assertTrue(adoptionCount.equals(totalAgents));

        Map<IAgentTag,Integer> tagCountMap = trait.getCurrentAdoptionCountsByTag();
        Set<IAgentTag> tags = tagCountMap.keySet();

        for(IAgentTag tag: tags) {
            log.info("tag: " + tag.getTagName() + " count: " + tagCountMap.get(tag));
        }

        log.info("agent with both tags is unadopting trait, all counts decline by one");

        trait.unadopt(agentBothForTest);
        Map<IAgentTag,Integer> tagCountMap2 = trait.getCurrentAdoptionCountsByTag();
        Set<IAgentTag> tags2 = tagCountMap2.keySet();

        for(IAgentTag tag: tags2) {
            log.info("tag: " + tag.getTagName() + " count: " + tagCountMap2.get(tag));
        }

        adoptionCount = trait.getCurrentAdoptionCount();
        assertTrue(adoptionCount.equals(totalAgents - 1));
        

        log.info("expected adopting agents: " + (totalAgents - 1) + " observed: " + adoptionCount);


    }


    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(IAgent.class).toProvider(UnstructuredTraitAgentProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
        binder.bind(IAgentTag.class).toProvider(SimpleAgentTagProvider.class);
        binder.bind(ITraitDimension.class).to(UnstructuredTraitDimension.class);
    }
}
