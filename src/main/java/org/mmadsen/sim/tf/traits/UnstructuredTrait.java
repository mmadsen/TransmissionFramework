/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.traits;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.*;
import com.google.common.base.Preconditions;


import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 9:18:36 AM
 * To change this template use File | Settings | File Templates.
 */


public class UnstructuredTrait implements ITrait {



    private String id;
    private Integer curAdoptionCount;
    private List<IAgent> curAdopteeList;
    private Map<Integer,Integer> histAdoptionCountMap;
    private Logger log;
    private ITraitDimension owningDimension = null;
    private ISimulationModel model;
    private Map<IAgentTag,Integer> curAdoptionByTag;

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
    }

    public ITraitDimension getOwningDimension() {
        return owningDimension;
    }

    public void setOwningDimension(ITraitDimension owningDimension) {
        this.owningDimension = owningDimension;
    }

    public UnstructuredTrait() {
        this.initialize();


    }

    private void initialize() {
        this.curAdoptionCount = new Integer(0);
        this.curAdopteeList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAdoptionCountMap = Collections.synchronizedMap(new HashMap<Integer,Integer>());
        this.curAdoptionByTag = Collections.synchronizedMap(new HashMap<IAgentTag,Integer>());
    }

    public String getTraitID() {
        return this.id;  
    }

    public void setTraitID(String id) {
        this.id = id;
    }

    public Integer getCurrentAdoptionCount() {
        return this.curAdoptionCount;
    }

    public Map<IAgentTag, Integer> getCurrentAdoptionCountsByTag() {
        return new HashMap<IAgentTag,Integer>(this.curAdoptionByTag);
    }

    public Integer getCurrentAdoptionCountForTag(IAgentTag tag) {
        return this.curAdoptionByTag.get(tag);
    }

    public Map<Integer,Integer> getAdoptionCountHistory() {
        return new HashMap<Integer,Integer>(this.histAdoptionCountMap);
    }

    public void adopt(IAgent agentAdopting) {
        Preconditions.checkNotNull(agentAdopting);

        // add the agent to the list for this trait
        // then register this trait with the agent adopting
        // then update this trait's counts per tag from the adopting agent
        log.trace("Agent " + agentAdopting.getAgentID() + " adopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.add(agentAdopting);
            agentAdopting.adoptTrait(this);
            this.incrementAdoptionCount();
        }
        synchronized(this.curAdoptionByTag) {
            Set<IAgentTag> tags = agentAdopting.getAgentTags();

            for(IAgentTag tag: tags) {

                if(this.curAdoptionByTag.containsKey(tag)) {
                    Integer count = this.curAdoptionByTag.get(tag);
                    
                    count++;
                    log.debug("trait: " + this.hashCode() + " current count: " + count);
                    this.curAdoptionByTag.put(tag,count);
                }
                else {
                    log.debug("trait: " + this.hashCode() + " initializing tag: " + tag.getTagName() + " with 1 count");
                    this.curAdoptionByTag.put(tag,1);
                }
            }
        }
    }

    public void unadopt(IAgent agentUnadopting) {
        Preconditions.checkNotNull(agentUnadopting);
        Preconditions.checkNotNull(model);

        // remove the agent from the list for this trait
        // then unregister this trait with the agent adopting
        // then update this trait's counts per tag from the unadopting agent
        // if a tag count gets to zero, we remove the tag from the list
        log.trace("Agent " + agentUnadopting.getAgentID() + " unadopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.remove(agentUnadopting);
            agentUnadopting.unadoptTrait(this);
            this.decrementAdoptionCount();
        }
        synchronized(this.curAdoptionByTag) {
            Set<IAgentTag> tags = agentUnadopting.getAgentTags();
            for(IAgentTag tag: tags) {
                if(this.curAdoptionByTag.containsKey(tag)) {
                    Integer count = this.curAdoptionByTag.get(tag);
                    count--;
                    if(count == 0) {
                        this.curAdoptionByTag.remove(tag);
                    } else {
                        this.curAdoptionByTag.put(tag,count);
                    }
                }
                else {
                    log.error("unadopting trait " + this.getTraitID() + " by agent " + agentUnadopting.getAgentID() + " with unknown tag - BUG");
                }
            }
        }

    }

    public List<IAgent> getCurrentAdopterList() {
        return new ArrayList<IAgent>(this.curAdopteeList);
    }

    public void clearAdoptionData() {
        this.curAdoptionCount = 0;
        this.curAdopteeList = null;
        this.histAdoptionCountMap = null;
        this.initialize();
    }

    private synchronized void incrementAdoptionCount() {
        this.curAdoptionCount++;
        this.histAdoptionCountMap.put(model.getCurrentModelTime(),this.curAdoptionCount);
    }

    private synchronized void decrementAdoptionCount() {
        this.curAdoptionCount--;
        this.histAdoptionCountMap.put(model.getCurrentModelTime(),this.curAdoptionCount);
    }
}