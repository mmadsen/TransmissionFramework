/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.classification;

import com.google.common.base.Preconditions;
import gnu.trove.map.hash.TIntIntHashMap;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.observers.ClassStatistic;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.interfaces.classification.IClass;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimensionMode;
import org.madsenlab.sim.tf.utils.ModeSetUniqueIdentifier;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 1/30/13
 * Time: 12:00 PM
 */

public class UnstructuredClass implements IClass {
    private Set<IClassDimensionMode> definingModeSet;
    private int curAdoptionCount;
    private List<IAgent> curAdopteeList;
    private Map<IAgentTag, Integer> curAdoptionByTag;
    private List<IStatisticsObserver> observers;
    private TIntIntHashMap histAdoptionCountMap;
    private ISimulationModel model;
    private Logger log;
    private Integer traitLifetime = 0;
    private Integer tickTraitIntroduced;
    private Integer tickTraitExited;
    private int uniqueObjectHashcode;
    private int classIDWithinClassification;

    /*
   * Two constructors, because sometimes we'll have an actual Set<Mode>
   * and sometimes (as when cartesianProduct() is used), we'll have a
   * List<Mode>
   */

    public UnstructuredClass(ISimulationModel model, Set<IClassDimensionMode> definingModes, Integer classID) {
        this.initialize(model, classID);
        this.definingModeSet = new HashSet<IClassDimensionMode>(definingModes);
        this.setObjectID();
    }

    public UnstructuredClass(ISimulationModel model, List<IClassDimensionMode> listDefiningModes, Integer classID) {
        this.initialize(model, classID);
        this.definingModeSet = new HashSet<IClassDimensionMode>(listDefiningModes);
        this.setObjectID();
    }

    private void setObjectID() {
        this.uniqueObjectHashcode = ModeSetUniqueIdentifier.getUniqueIdentifier(this.definingModeSet);
    }

    private void initialize(ISimulationModel model, Integer classID) {
        this.model = model;
        this.classIDWithinClassification = classID;
        this.tickTraitIntroduced = this.model.getCurrentModelTime();
        log = model.getModelLogger(this.getClass());
        this.curAdoptionCount = 0;
        this.curAdopteeList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAdoptionCountMap = new TIntIntHashMap();
        this.curAdoptionByTag = Collections.synchronizedMap(new HashMap<IAgentTag, Integer>());
        this.observers = Collections.synchronizedList(new ArrayList<IStatisticsObserver>());
    }


    @Override
    public Integer getClassIDWithinClassification() {
        return this.classIDWithinClassification;
    }

    @Override
    public Set<IClassDimensionMode> getModesDefiningClass() {
        return new HashSet<IClassDimensionMode>(this.definingModeSet);
    }

    @Override
    public String getClassDescription() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        for (IClassDimensionMode mode : this.definingModeSet) {
            sb.append(mode.getOwningClassDimension().getClassDimensionName());
            sb.append(":");
            sb.append(mode.getModeDescription());
            sb.append(";");
        }
        sb.append("}");
        return sb.toString();
    }

    public String toString() {
        return this.getClassDescription();
    }

    @Override
    public int getCurrentAdoptionCount() {
        return this.curAdoptionCount;
    }

    public Map<IAgentTag, Integer> getCurrentAdoptionCountsByTag() {
        return new HashMap<IAgentTag, Integer>(this.curAdoptionByTag);
    }

    public Integer getCurrentAdoptionCountForTag(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Retrieving an adoption count by tag requires a non-null reference to an IAgentTag object");
        Integer count = this.curAdoptionByTag.get(tag);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    public void adopt(IAgent agentAdopting) {

        // add the agent to the list for this trait
        // then register this trait with the agent adopting
        // then update this trait's counts per tag from the adopting agent

        synchronized (this.curAdopteeList) {
            this.curAdopteeList.add(agentAdopting);
            this.incrementAdoptionCount();
            log.trace("Agent " + agentAdopting.getAgentID() + " adopting " + this.getClassDescription() + " count: " + this.curAdoptionCount);
        }
        synchronized (this.curAdoptionByTag) {
            Set<IAgentTag> tags = agentAdopting.getAgentTags();

            for (IAgentTag tag : tags) {

                if (this.curAdoptionByTag.containsKey(tag)) {
                    int count = this.curAdoptionByTag.get(tag);

                    count++;
                    log.trace("" + this.getClassDescription() + " count: " + count);
                    this.curAdoptionByTag.put(tag, count);
                } else {
                    log.trace("" + this.getClassDescription() + " initializing tag: " + tag.getTagName() + " with 1 count");
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
        log.trace("Agent " + agentUnadopting.getAgentID() + " unadopting trait " + this.getClassDescription() + " count: " + this.curAdoptionCount);
        synchronized (this.curAdopteeList) {
            this.curAdopteeList.remove(agentUnadopting);
            this.decrementAdoptionCount();
            //this.checkDurationAtAdoptionCountChange();
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
                    log.error("unadopting trait " + this.getClassDescription() + " by agent " + agentUnadopting.getAgentID() + " with unknown tag - BUG");
                }
            }
        }
    }

    @Override
    public List<IAgent> getCurrentAdopterList() {
        return new ArrayList<IAgent>(this.curAdopteeList);
    }

    @Override
    public Boolean hasCompleteDuration() {
        return null;
    }

    @Override
    public Integer getTraitDuration() {
        return null;
    }

    @Override
    public int getUniqueID() {
        return this.uniqueObjectHashcode;
    }

    @Override
    public int compareTo(IClass otherClass) {
        return this.getClassDescription().compareTo(otherClass.getClassDescription());
    }

    public void attach(IStatisticsObserver obs) {
        synchronized (this.observers) {
            log.trace("attaching to obs: " + obs);
            this.observers.add(obs);
        }
    }

    public void attach(List<IStatisticsObserver> obsList) {
        for (IStatisticsObserver obs : obsList) {
            this.attach(obs);
        }
    }

    public void detach(IStatisticsObserver obs) {
        synchronized (this.observers) {
            this.observers.remove(obs);
        }

    }

    public void detach(List<IStatisticsObserver> obsList) {
        for (IStatisticsObserver obs : obsList) {
            this.detach(obs);
        }
    }

    public Integer getNumObservers() {
        return this.observers.size();
    }

    public void notifyObservers() {
        IStatistic stat = this.getChangeStatistic();
        log.debug("change statistic: " + stat);
        for (IStatisticsObserver obs : this.observers) {
            log.debug("notify observer: " + obs);
            obs.updateStatistics(stat);
        }
    }

    public void clearAdoptionData() {
        this.curAdoptionCount = 0;
        this.curAdopteeList = null;
        this.histAdoptionCountMap = null;
        this.initialize(this.model, this.classIDWithinClassification);
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

    public IStatistic getChangeStatistic() {
        return new ClassStatistic(this, model.getCurrentModelTime());
    }
}

