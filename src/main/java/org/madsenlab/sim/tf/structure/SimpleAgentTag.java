/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.structure;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.utils.AgentTagType;

import java.util.*;

public class SimpleAgentTag implements IAgentTag {

    private String tagname;
    private Integer curAgentCount;
    private List<IAgent> agentList;
    private Map<Integer, Integer> histAgentCountMap;
    private ISimulationModel model;
    private Logger log;
    private AgentTagType type;

    public SimpleAgentTag() {
        this.initialize();
    }

    @Inject
    public void setSimulationModel(ISimulationModel model) {
        this.model = model;
        log = this.model.getModelLogger(this.getClass());
    }

    private void initialize() {
        this.curAgentCount = 0;
        this.agentList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAgentCountMap = Collections.synchronizedMap(new HashMap<Integer, Integer>());
    }

    public String getTagName() {
        return this.tagname;
    }

    public void setTagName(String tagname) {
        this.tagname = tagname;
    }

    public List<IAgent> getCurAgentsTagged() {
        return new ArrayList<IAgent>(this.agentList);
    }

    public void registerAgent(IAgent agent) {
        Preconditions.checkNotNull(agent);
        this.incrementAdoptionCount();
        log.trace("Agent registering:" + agent.getAgentID());
        synchronized (this.agentList) {
            if (this.agentList.contains(agent) == false) {
                this.agentList.add(agent);
                agent.addTag(this);
            }
        }
    }

    public void unregisterAgent(IAgent agent) {
        Preconditions.checkNotNull(agent);
        this.decrementAdoptionCount();
        log.trace("Agent unregistering: " + agent.getAgentID());
        synchronized (this.agentList) {
            this.agentList.remove(agent);
            agent.removeTag(this);
        }
    }

    public Integer curAgentCount() {
        return this.curAgentCount;
    }

    public Map<Integer, Integer> getAgentCountHistory() {
        return new HashMap<Integer, Integer>(this.histAgentCountMap);
    }

    public void setTagType(AgentTagType type) {
        this.type = type;
    }

    public AgentTagType getTagType() {
        return this.type;
    }

    private synchronized void incrementAdoptionCount() {
        this.curAgentCount++;
        this.histAgentCountMap.put(this.model.getCurrentModelTime(), this.curAgentCount);
    }

    private synchronized void decrementAdoptionCount() {
        this.curAgentCount--;
        this.histAgentCountMap.put(this.model.getCurrentModelTime(), this.curAgentCount);
    }
}
