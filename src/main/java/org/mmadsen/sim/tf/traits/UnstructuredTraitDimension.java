package org.mmadsen.sim.tf.traits;

import com.google.inject.Inject;
import com.google.inject.internal.Preconditions;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 1, 2010
 * Time: 2:19:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnstructuredTraitDimension implements ITraitDimension {
    String dimensionName = null;
    Map<String,ITrait> traitMap = null;
    Logger log;
    ISimulationModel model;

    @Inject
    public void setSimulationModel(ISimulationModel model) {
        this.model = model;
        log = model.getModelLogger(this.getClass());
    }

    public UnstructuredTraitDimension() {
        this.initialize();
    }

    private void initialize() {
        this.traitMap = new HashMap<String,ITrait>();
    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public void addTrait(ITrait newTrait) {
        this.traitMap.put(newTrait.getTraitID(),newTrait);
    }

    public ITrait getTrait(String traitID) {
        return this.traitMap.get(traitID);
    }

    public Collection<ITrait> getTraitsInDimension() {
        return this.traitMap.values();
    }

    public Map<String, Integer> getCurGlobalTraitCounts() {
        Map<String,Integer> countMap = new HashMap<String,Integer>();

        for(ITrait trait: this.traitMap.values()) {
            countMap.put(trait.getTraitID(), trait.getCurrentAdoptionCount());
        }
        return countMap;
    }

    public Map<String, Double> getCurGlobalTraitFrequencies() {
        Preconditions.checkNotNull(model);
        log.info("getting current trait frequency map");
        Map<String,Double> freqMap = new HashMap<String,Double>();
        Integer total = 0;

        Integer popsize = this.model.getCurrentPopulationSize();
        for(ITrait trait: this.traitMap.values()) {
            double freq = (double) trait.getCurrentAdoptionCount() / (double) popsize;
            freqMap.put(trait.getTraitID(), freq);
        }
        return freqMap;  
    }

//    public void removeTrait(ITrait traitToRemove) {
//        this.traitMap.remove(traitToRemove);
//    }
}
