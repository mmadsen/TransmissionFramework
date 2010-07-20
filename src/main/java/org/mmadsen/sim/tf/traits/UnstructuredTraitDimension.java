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
import com.google.inject.internal.Preconditions;
import org.apache.log4j.Logger;
import org.mmadsen.sim.tf.interfaces.IAgentTag;
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
    private ISimulationModel model;

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
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

    public Map<ITrait, Integer> getCurGlobalTraitCounts() {
        Map<ITrait,Integer> countMap = new HashMap<ITrait,Integer>();

        for(ITrait trait: this.traitMap.values()) {
            countMap.put(trait, trait.getCurrentAdoptionCount());
        }
        return countMap;
    }

    public Map<ITrait, Double> getCurGlobalTraitFrequencies() {
        Preconditions.checkNotNull(model);
        log.info("getting current trait frequency map");
        Map<ITrait,Double> freqMap = new HashMap<ITrait,Double>();
        Integer total = 0;

        Integer popsize = this.model.getCurrentPopulationSize();
        for(ITrait trait: this.traitMap.values()) {
            double freq = (double) trait.getCurrentAdoptionCount() / (double) popsize;
            freqMap.put(trait, freq);
        }
        return freqMap;  
    }

    public Map<ITrait, Integer> getCurTraitCountByTag(IAgentTag tag) {
        return null;
    }

    public Map<ITrait, Double> getCurTraitFreqByTag(IAgentTag tag) {
        return null;
    }


    public void removeTrait(ITrait traitToRemove) {
        this.traitMap.remove(traitToRemove);
    }
}
