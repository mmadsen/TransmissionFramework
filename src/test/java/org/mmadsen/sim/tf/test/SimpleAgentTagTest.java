/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.test;

import static org.junit.Assert.*;
import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.inject.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mmadsen.sim.tf.agent.SimpleAgentProvider;
import org.mmadsen.sim.tf.interfaces.*;
import org.mmadsen.sim.tf.structure.SimpleAgentTag;
import org.mmadsen.sim.tf.traits.UnstructuredTraitDimensionProvider;
import org.mmadsen.sim.tf.traits.UnstructuredTraitProvider;

import java.util.List;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class SimpleAgentTagTest implements Module {
    @Inject @Unit
    private IAgentTag tag;
    @Inject private IAgent agent;
    @Inject private ISimulationModel model;
    @Inject private Provider<IAgent> agentProvider;
    private Logger log;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSetTagName() throws Exception {
        log.info("Entering testSetTagName");
        String expected = "testtag";
        tag.setTagName(expected);
        assertEquals(tag.getTagName(),expected);
        log.info("Exiting testSetTagName");
    }

    @Test
    public void testRegisterAgent() throws Exception {
        log.info("Entering testRegisterAgent");
        addNewAgentToTag(tag);
        Integer agentCount = tag.curAgentCount();
        log.info("expected: 1  observed: " + agentCount);
        assertTrue(agentCount == 1);
        log.info("Exiting testRegisterAgent");
    }

    public void addNewAgentToTag(IAgentTag tag) {
        IAgent agent = agentProvider.get();
        //log.info("new agent: " + agent);
        tag.registerAgent(agent);
    }

    @Test
    public void testUnregisterAgent() throws Exception {
        log.info("Entering testUnregisterAgent");
        log.info("adding 3 new agents, then unregistering the first");
        addNewAgentToTag(tag);
        addNewAgentToTag(tag);
        addNewAgentToTag(tag);

        List<IAgent> agentList = tag.getCurAgentsTagged();
        IAgent agent0 = agentList.get(0);
        tag.unregisterAgent(agent0);

        Integer agentCount = tag.curAgentCount();
        log.info("expected: 2  observed: " + agentCount);
        assertTrue(agentCount == 2);
        log.info("Exiting testUnregisterAgent");
    }


    @Test
    public void testGetAgentCountHistory() throws Exception {
        log.info("Entering testGetAgentCountHistory");


        assertTrue(false);
        log.info("Exiting testGetAgentCountHistory");
    }

    public void configure(Binder binder) {
        binder.bind(ITrait.class).toProvider(UnstructuredTraitProvider.class);
        binder.bind(ITraitDimension.class).toProvider(UnstructuredTraitDimensionProvider.class);
        binder.bind(IAgent.class).toProvider(SimpleAgentProvider.class);
        binder.bind(ISimulationModel.class).to(SimulationModelFixture.class);
        binder.bind(IAgentTag.class).to(SimpleAgentTag.class);
    }

}