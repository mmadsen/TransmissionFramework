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
    private ISimulationModel simulationModel;
    private Logger log;
    private ITraitDimension owningDimension = null;

    @Inject
    public void setSimulationModel(ISimulationModel model) {
        simulationModel = model;
        log = simulationModel.getModelLogger(this.getClass());
    }

    public ITraitDimension getOwningDimension() {
        return owningDimension;
    }

    public void setOwningDimension(ITraitDimension owningDimension) {
        this.owningDimension = owningDimension;
    }

    public UnstructuredTrait() {
        this.initAdoptionData();

    }

    private void initAdoptionData() {
        this.curAdoptionCount = new Integer(0);
        this.curAdopteeList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAdoptionCountMap = Collections.synchronizedMap(new HashMap<Integer,Integer>());
    }

    public String getTraitID() {
        return this.id;  
    }

    @Inject
    public void setTraitID(String id) {
        this.id = id;
    }

    public Integer getCurrentAdoptionCount() {
        return this.curAdoptionCount;
    }

    public Map<Integer,Integer> getAdoptionCountHistory() {
        return this.histAdoptionCountMap;
    }

    public void adopt(IAgent agentAdopting) {
        Preconditions.checkNotNull(agentAdopting);
        this.incrementAdoptionCount();
        log.trace("Agent " + agentAdopting.getAgentID() + " adopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.add(agentAdopting);
        }
    }

    public void unadopt(IAgent agentUnadopting) {
        Preconditions.checkNotNull(agentUnadopting);
        this.decrementAdoptionCount();
        log.trace("Agent " + agentUnadopting.getAgentID() + " unadopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.remove(agentUnadopting);
        }
    }

    public List<IAgent> getCurrentAdopterList() {
        return this.curAdopteeList;
    }

    public void clearAdoptionData() {
        this.curAdoptionCount = 0;
        this.curAdopteeList = null;
        this.histAdoptionCountMap = null;
        this.initAdoptionData();
    }

    private synchronized void incrementAdoptionCount() {
        this.curAdoptionCount++;
        this.histAdoptionCountMap.put(this.simulationModel.getCurrentModelTime(),this.curAdoptionCount);
    }

    private synchronized void decrementAdoptionCount() {
        this.curAdoptionCount--;
        this.histAdoptionCountMap.put(this.simulationModel.getCurrentModelTime(),this.curAdoptionCount);
    }
}
