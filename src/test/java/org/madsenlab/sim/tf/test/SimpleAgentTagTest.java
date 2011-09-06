/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
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
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.test.util.AbstractGuiceTestClass;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class SimpleAgentTagTest extends AbstractGuiceTestClass implements Module {
    @Inject
    @Unit
    private IAgentTag tag;
    @Inject
    private IAgent agent;
    @Inject
    private ISimulationModel model;
    private Logger log;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();

    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
    }

    @Test
    public void testSetTagName() throws Exception {
        log.info("Entering testSetTagName");
        String expected = "testtag";
        tag.setTagName(expected);
        assertEquals(tag.getTagName(), expected);
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
        IAgent agent = model.getPopulation().createAgent();
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

    @Ignore
    @Test
    public void testGetAgentCountHistory() throws Exception {
        log.info("Entering testGetAgentCountHistory");


        assertTrue(false);
        log.info("Exiting testGetAgentCountHistory");
    }



}
