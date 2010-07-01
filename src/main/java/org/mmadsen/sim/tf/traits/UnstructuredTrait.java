package org.mmadsen.sim.tf.traits;

import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.exceptions.UnimplementedMethodException;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IModelGlobals;
import org.mmadsen.sim.tf.interfaces.ITrait;

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

    public UnstructuredTrait(String id, IModelGlobals model) {
        this.curAdoptionCount = new Integer(0);
        this.curAdopteeList = Collections.synchronizedList(new ArrayList<IAgent>());
        this.histAdoptionCountMap = Collections.synchronizedMap(new HashMap<Integer,Integer>());
        this.id = id;
        this.model = model;
        this.log = this.model.getModelLogger();
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
        throw new UnimplementedMethodException(UnstructuredTrait.class,"clearAdoptionData");
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
