/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.traits;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import gnu.trove.map.hash.TIntIntHashMap;
import org.madsenlab.sim.tf.analysis.TraitStatistic;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 9:18:36 AM
 * To change this template use File | Settings | File Templates.
 */


public class UnstructuredTrait extends AbstractObservableTrait implements ITrait {
    private String id;
    private int curAdoptionCount;
    private List<IAgent> curAdopteeList;
    private TIntIntHashMap histAdoptionCountMap;

    private ITraitDimension owningDimension = null;

    private Map<IAgentTag, Integer> curAdoptionByTag;


    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        this.tickTraitIntroduced = this.model.getCurrentModelTime();
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
        this.curAdoptionCount = 0;
        this.curAdopteeList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAdoptionCountMap = new TIntIntHashMap();
        this.curAdoptionByTag = Collections.synchronizedMap(new HashMap<IAgentTag, Integer>());
        this.observers = Collections.synchronizedList(new ArrayList<ITraitStatisticsObserver>());
    }

    public String getTraitID() {
        return this.id;
    }

    public void setTraitID(String id) {
        this.id = id;
    }

    public int getCurrentAdoptionCount() {
        return this.curAdoptionCount;
    }

    public Map<IAgentTag, Integer> getCurrentAdoptionCountsByTag() {
        return new HashMap<IAgentTag, Integer>(this.curAdoptionByTag);
    }

    public Integer getCurrentAdoptionCountForTag(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Retrieving an adoption count by tag requires a non-null reference to an IAgentTag object");
        return this.curAdoptionByTag.get(tag);
    }

    public TIntIntHashMap getAdoptionCountHistory() {
        return this.histAdoptionCountMap;
    }

    public void adopt(IAgent agentAdopting) {

        // add the agent to the list for this trait
        // then register this trait with the agent adopting
        // then update this trait's counts per tag from the adopting agent

        synchronized (this.curAdopteeList) {
            this.curAdopteeList.add(agentAdopting);
            agentAdopting.adoptTrait(this);
            this.incrementAdoptionCount();
            log.trace("Agent " + agentAdopting.getAgentID() + " adopting [" + this.getTraitID() + "] count: " + this.curAdoptionCount);
        }
        synchronized (this.curAdoptionByTag) {
            Set<IAgentTag> tags = agentAdopting.getAgentTags();

            for (IAgentTag tag : tags) {

                if (this.curAdoptionByTag.containsKey(tag)) {
                    int count = this.curAdoptionByTag.get(tag);

                    count++;
                    log.trace("[" + this.getTraitID() + "] count: " + count);
                    this.curAdoptionByTag.put(tag, count);
                } else {
                    log.trace("[" + this.getTraitID() + "] initializing tag: " + tag.getTagName() + " with 1 count");
                    this.curAdoptionByTag.put(tag, 1);
                }
            }
        }
    }

    public void unadopt(IAgent agentUnadopting) {

        // remove the agent from the list for this trait
        // then unregister this trait with the agent adopting
        // then update this trait's counts per tag from the unadopting agent
        // if a tag count gets to zero, we remove the tag from the list
        log.trace("Agent " + agentUnadopting.getAgentID() + " unadopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized (this.curAdopteeList) {
            this.curAdopteeList.remove(agentUnadopting);
            agentUnadopting.unadoptTrait(this);
            this.decrementAdoptionCount();
            this.checkDurationAtAdoptionCountChange();
        }
        synchronized (this.curAdoptionByTag) {

            Set<IAgentTag> tags = agentUnadopting.getAgentTags();
            for (IAgentTag tag : tags) {
                if (this.curAdoptionByTag.containsKey(tag)) {
                    int count = this.curAdoptionByTag.get(tag);
                    count--;
                    if (count == 0) {
                        this.curAdoptionByTag.remove(tag);
                    } else {
                        this.curAdoptionByTag.put(tag, count);
                    }
                } else {
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
        this.histAdoptionCountMap.put(model.getCurrentModelTime(), this.curAdoptionCount);
    }

    private synchronized void decrementAdoptionCount() {
        this.curAdoptionCount--;
        this.histAdoptionCountMap.put(model.getCurrentModelTime(), this.curAdoptionCount);
    }

    private void checkDurationAtAdoptionCountChange() {
        if (this.curAdoptionCount == 0) {
            this.tickTraitExited = this.model.getCurrentModelTime();
            this.traitLifetime = this.tickTraitExited - this.tickTraitIntroduced;
        }
    }


    public ITraitStatistic getChangeStatistic() {
        return new TraitStatistic(this.owningDimension, model.getCurrentModelTime());
    }
}
