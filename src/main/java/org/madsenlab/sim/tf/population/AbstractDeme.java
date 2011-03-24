/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.IDeme;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.utils.AgentPredicate;
import org.madsenlab.sim.tf.utils.AgentTagPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractDeme provides the base class and most of the implementation for dealing with
 * subpopulations of agents, apart from agent creation and destruction.  Additional methods
 * which should be available on any partition of the population should be added here.
 * <p/>
 * Subclasses should provide a means of initializing the population and ensuring valid references
 * to the ISimulationModel which implements a given agent-based simulation.  If a subclass provides
 * only initialization services, and only implements IDeme, the resulting IDeme object will be
 * functionally read-only.
 * <p/>
 * If a subclass provides agent creation/destruction methods (e.g., by implementing IPopulation),
 * the resulting object will be mutable and capable of full population dynamics.
 * <p/>
 * <p/>
 * <p/>
 * <p/>
 * User: mark
 * Date: Sep 30, 2010
 * Time: 2:17:12 PM
 */

public class AbstractDeme implements IDeme {
    protected ISimulationModel model;
    protected Logger log;
    protected List<IAgent> agentList;
    @Inject
    protected Provider<IDeme> demeProvider;

    public void setAgentList(List<IAgent> agents) {
        this.agentList = agents;
    }

    public List<IAgent> getAgents() {
        return new ArrayList<IAgent>(agentList);
    }

    public IAgent getAgentAtRandom() {
        Integer randomAgentNumber = this.model.getUniformRandomInteger(this.getCurrentPopulationSize());
        return this.agentList.get(randomAgentNumber);
    }

    public IDeme getDemeForTag(IAgentTag tag) {
        AgentPredicate pred = new AgentTagPredicate(tag);
        return getDemeMatchingPredicate(pred);
    }

    public IDeme getDemeMatchingPredicate(AgentPredicate pred) {
        log.trace("demeProvider: " + demeProvider);
        List<IAgent> matches = new ArrayList<IAgent>();

        for (IAgent agent : agentList) {
            if (pred.evaluate(agent)) {
                matches.add(agent);
                //log.info("adding agent to match list: " + agent);
            }
        }

        log.debug("size of match list: " + matches.size());

        IDeme subDeme = demeProvider.get();
        subDeme.setAgentList(matches);

        return subDeme;
    }

    public Integer getCurrentPopulationSize() {
        log.trace("agentList size: " + this.agentList.size());
        return this.agentList.size();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Boolean hasMemberAgents() {
        if (getCurrentPopulationSize() == 0) {
            return false;
        } else return true;
    }

}
