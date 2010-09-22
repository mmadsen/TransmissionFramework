/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IAgentTag;
import org.mmadsen.sim.tf.interfaces.IPopulation;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 8, 2010
 * Time: 1:27:08 PM
 */

public class SimpleAgentPopulation implements IPopulation {
    
    private Provider<IAgent> agentProvider;
    private ISimulationModel model;
    private Logger log;
    private static List<IAgent> agentList;


    @Inject
    public void initialize(ISimulationModel m, Provider<IAgent> p) {
        model = m;
        agentProvider = p;
        log = model.getModelLogger(this.getClass());
        if(agentList == null) {
            agentList = Collections.synchronizedList(new ArrayList<IAgent>());
        }

        log.trace("model: " + model + " agentProvider: " + agentProvider);

    }

    public List<IAgent> getAgents() {
        return new ArrayList<IAgent>(agentList);
    }

    public IPopulation getSubpopulationForTag(IAgentTag tag) {
        return null;
    }

    public IAgent getAgentAtRandom() {
        return null;
    }

    public IPopulation getSubpopulationMatchingPredicate(Predicate pred) {
        return null;
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

    public void clearAgentPopulation() {
        synchronized(agentList) {
            agentList.clear();
        }
    }
}
