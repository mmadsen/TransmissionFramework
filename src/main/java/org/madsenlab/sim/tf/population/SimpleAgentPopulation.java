/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 8, 2010
 * Time: 1:27:08 PM
 */

public class SimpleAgentPopulation extends AbstractDeme implements IPopulation, IDeme {

    private Provider<IAgent> agentProvider;

    @Inject
    public void initialize(ISimulationModel m, Provider<IAgent> p, Provider<IDeme> d) {
        model = m;
        agentProvider = p;
        demeProvider = d;
        log = model.getModelLogger(this.getClass());
        if (agentList == null) {
            agentList = Collections.synchronizedList(new ArrayList<IAgent>());
        }

        log.trace("model: " + model + " agentProvider: " + agentProvider + " demeProvider: " + demeProvider);

    }


    public IAgent createAgent() {

        IAgent newAgent = agentProvider.get();
        // do stuff to new agent

        // now register the agent in the manager's list, and with the
        // simulation model
        synchronized (agentList) {
            agentList.add(newAgent);
            log.trace("New agent created and registered: " + newAgent);
            log.trace("Population size now: " + agentList.size());
        }

        return newAgent;
    }

    public IAgent createAgentWithTag(IAgentTag tag) {
        IAgent newAgent = this.createAgent();
        log.trace("adding tag [" + tag + "] to new agent");
        newAgent.addTag(tag);
        tag.registerAgent(newAgent);
        return newAgent;
    }

    public void removeAgent(IAgent agent) {
        synchronized (agentList) {
            agentList.remove(agent);
            log.trace("Agent " + agent + " removed and unregistered");
            log.trace("Population size now: " + agentList.size());
        }

        agent = null;
    }

    public void clearAgentPopulation() {
        synchronized (agentList) {
            agentList.clear();
        }
    }

    @Override
    public void savePreviousStepTraits() {
        log.debug("entering savePreviousStepTraits for population");
        for (IAgent agent : this.agentList) {
            agent.savePreviousStepTraits();
        }
    }
}
