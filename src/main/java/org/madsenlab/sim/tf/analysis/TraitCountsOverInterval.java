/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.  
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.analysis;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.exceptions.TraitCountIntervalFullException;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/15/12
 * Time: 11:46 AM
 */

public class TraitCountsOverInterval {
    private ISimulationModel model;
    private Logger log;
    private Integer windowSize;

    public Integer getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Integer windowSize) {
        this.windowSize = windowSize;
    }

    public Integer getIntervalStart() {
        return intervalStart;
    }

    public void setIntervalStart(Integer intervalStart) {
        this.intervalStart = intervalStart;
    }

    public Integer getIntervalEnd() {
        return intervalEnd;
    }

    private Integer intervalStart;
    private Integer intervalEnd;
    private Map<ITrait, Integer> intervalCountMap;
    private Integer numSlotsFilled;
    
    public TraitCountsOverInterval(ISimulationModel m, Integer windowSize) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.windowSize = windowSize;
        this.numSlotsFilled = 0;
        this.intervalCountMap = new HashMap<ITrait, Integer>();
    }

    public void startCounting(Integer startTime) {
        this.intervalStart = startTime;
    }

    public void endCounting(Integer endTime) {
        this.intervalEnd = endTime;
    }
    
    public Boolean hasCapacity() {
        if(this.numSlotsFilled < this.windowSize) {
            return true;
        } else { return false; }
    }

    public void putTraitCountsForTick(Map<ITrait, Integer> countMap) {
        if(! this.hasCapacity()) { throw new TraitCountIntervalFullException(this.getClass(), "This TraitCountsOverIntervalObject is full"); }
        Set<ITrait> traitSet = countMap.keySet();
        for(ITrait trait: traitSet) {
            if(this.intervalCountMap.containsKey(trait)) {
                int curCount = this.intervalCountMap.get(trait);
                curCount += countMap.get(trait);
                this.intervalCountMap.put(trait,curCount);
            } else {
                // we haven't seen this trait before in this interval, so initialize it with the incoming count (not 1).  
                this.intervalCountMap.put(trait,countMap.get(trait));
            }
        }
        this.numSlotsFilled++;
    }

    public Map<ITrait,Integer> getIntervalCountMap() {
        return this.intervalCountMap;
    }
    
}
