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
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;
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

    public Map<Integer,Integer> getAdoptionCountHistory() {
        return new HashMap<Integer,Integer>(this.histAdoptionCountMap);
    }

    public void adopt(IAgent agentAdopting) {
        Preconditions.checkNotNull(agentAdopting);
        Preconditions.checkNotNull(model);
        this.incrementAdoptionCount();
        log.trace("Agent " + agentAdopting.getAgentID() + " adopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.add(agentAdopting);
        }
    }

    public void unadopt(IAgent agentUnadopting) {
        Preconditions.checkNotNull(agentUnadopting);
        Preconditions.checkNotNull(model);
        this.decrementAdoptionCount();
        log.trace("Agent " + agentUnadopting.getAgentID() + " unadopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.remove(agentUnadopting);
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
