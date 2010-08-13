/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.models;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSimModel implements ISimulationModel
{
    protected Logger log;
    protected Integer currentTime = 0;
    private static List<IAgent> agentList;
    @Inject public
    Provider<IAgent> agentProvider;
    @Inject public
    Provider<ITrait> traitProvider;
    @Inject public Provider<ITraitDimension> dimensionProvider;

    public AbstractSimModel() {
        log = Logger.getLogger(this.getClass());
        log.trace("log4j configured and ready");
        if(agentList == null) {
            log.trace("AgentList being initialized");
            agentList = Collections.synchronizedList(new ArrayList<IAgent>());
        }
    }

    public Integer getCurrentModelTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Logger getModelLogger(Class classToLog) {
        return Logger.getLogger(classToLog);
    }

    public Integer getCurrentPopulationSize() {
        return agentList.size();  //To change body of implemented methods use File | Settings | File Templates.
    }


    public IAgent createAgent() {
        IAgent newAgent = agentProvider.get();
        // do stuff to new agent

        // now register the agent in the manager's list, and with the
        // simulation model
        synchronized(agentList) {
            agentList.add(newAgent);
            log.trace("New agent created and registered: " + newAgent);
            log.trace("Population size now: " + agentList.size());
        }

        return newAgent;
    }

    public void removeAgent(IAgent agent) {
        synchronized(agentList) {
            agentList.remove(agent);
            log.trace("Agent " + agent + " removed and unregistered");
            log.trace("Population size now: " + agentList.size());
        }

        agent = null;
    }

    public Provider<ITrait> getTraitProvider() {
        return traitProvider;
    }

    public Provider<ITraitDimension> getTraitDimensionProvider() {
        return dimensionProvider;
    }


    public void clearAgentPopulation() {
        agentList.clear();
    }
}
