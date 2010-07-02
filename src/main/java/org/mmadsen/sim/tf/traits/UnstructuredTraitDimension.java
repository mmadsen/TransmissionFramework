package org.mmadsen.sim.tf.traits;

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

    public UnstructuredTraitDimension(String dimensionName) {
        this.dimensionName = dimensionName;
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

    public Map<String, Integer> getCurrentTraitCountMap() {
        Map<String,Integer> countMap = new HashMap<String,Integer>();

        for(ITrait trait: this.traitMap.values()) {
            countMap.put(trait.getTraitID(), trait.getCurrentAdoptionCount());
        }
        return countMap;
    }

    public Map<String, Double> getCurrentTraitFreqMap() {
        Map<String,Double> freqMap = new HashMap<String,Double>();
        Integer total = 0;

        /* I hate the inefficiency here...maybe I ought to just keep population size around???  */
        for(ITrait trait: this.traitMap.values()) {
            total += trait.getCurrentAdoptionCount();
        }
        for(ITrait trait: this.traitMap.values()) {
            double freq = (double) trait.getCurrentAdoptionCount() / (double) total;
            freqMap.put(trait.getTraitID(), freq);
        }
        return freqMap;  
    }

    public void removeTrait(ITrait traitToRemove) {
        this.traitMap.remove(traitToRemove);
    }
}
