package org.mmadsen.sim.tf.traits;

import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.exceptions.UnimplementedMethodException;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IModelGlobals;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 9:18:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class UnstructuredTrait implements ITrait {

    String id;
    Integer curAdoptionCount;
    List<IAgent> curAdopteeList;
    Map<Integer,Integer> histAdoptionCountMap;
    IModelGlobals model;
    Logger log;
    ITraitDimension owningDimension = null;

    public ITraitDimension getOwningDimension() {
        return owningDimension;
    }

    public void setOwningDimension(ITraitDimension owningDimension) {
        this.owningDimension = owningDimension;
    }

    public UnstructuredTrait(String id, IModelGlobals model) {
        this.initAdoptionData();
        this.id = id;
        this.model = model;
        this.log = this.model.getModelLogger();
    }

    private void initAdoptionData() {
        this.curAdoptionCount = new Integer(0);
        this.curAdopteeList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAdoptionCountMap = Collections.synchronizedMap(new HashMap<Integer,Integer>());
    }

    public String getTraitID() {
        return this.id;  
    }

    public Integer getCurrentAdoptionCount() {
        return this.curAdoptionCount;
    }

    public Map<Integer,Integer> getAdoptionCountHistory() {
        return this.histAdoptionCountMap;
    }

    public void adopt(IAgent agentAdopting) {
        this.incrementAdoptionCount();
        log.debug("Agent " + agentAdopting.getAgentID() + " adopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
        synchronized(this.curAdopteeList) {
            this.curAdopteeList.add(agentAdopting);
        }
    }

    public void unadopt(IAgent agentUnadopting) {
        this.decrementAdoptionCount();
        log.debug("Agent " + agentUnadopting.getAgentID() + " unadopting trait " + this.getTraitID() + " count: " + this.curAdoptionCount);
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
        this.histAdoptionCountMap.put(this.model.getCurrentModelTime(),this.curAdoptionCount);
    }

    private synchronized void decrementAdoptionCount() {
        this.curAdoptionCount--;
        this.histAdoptionCountMap.put(this.model.getCurrentModelTime(),this.curAdoptionCount);
    }
}
