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
import org.mmadsen.sim.tf.interfaces.*;

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

        log.info("model: " + model + " population: " + model.getPopulation());

        Integer popsize = this.model.getPopulation().getCurrentPopulationSize();
        for(ITrait trait: this.traitMap.values()) {
            double freq = (double) trait.getCurrentAdoptionCount() / (double) popsize;
            freqMap.put(trait, freq);
        }
        return freqMap;  
    }

    public Map<ITrait, Integer> getCurTraitCountByTag(IAgentTag tag) {
        log.debug("Entering getCurTraitCountByTag");
        Preconditions.checkNotNull(tag);
        // We use a Set to gather a unique list of the traits held by all
        // agents tagged with the tag specified.    
        Set<ITrait> traitsAdopted = new HashSet<ITrait>();
        List<IAgent> agentList = tag.getCurAgentsTagged();

        //log.debug("agents tagged: " + agentList.size())


        for(IAgent agent: agentList) {
            Set<ITrait> myTraits = agent.getCurrentlyAdoptedTraits();

            for(ITrait aTrait: myTraits) {
                //log.debug("trait added to traitsAdopted: " + aTrait);
                traitsAdopted.add(aTrait);
            }
        }

        //log.debug("traitsAdopted size: " + traitsAdopted.size());

        // Now we go through and ask each trait its adoption count for
        // this tag
        Map<ITrait,Integer> adoptionCountMap = new HashMap<ITrait,Integer>();
        for(ITrait trait: traitsAdopted) {

            Integer count = trait.getCurrentAdoptionCountForTag(tag);
            log.debug("trait: " + trait + " count: " + count);
            adoptionCountMap.put(trait,count);
        }
        

        log.debug("Exiting getCurTraitCountByTag");
        return adoptionCountMap;
    }



    public Map<ITrait, Double> getCurTraitFreqByTag(IAgentTag tag) {
        Integer agentCountForTag = tag.curAgentCount();
        Map<ITrait,Integer> adoptionCountMap = this.getCurTraitCountByTag(tag);
        Map<ITrait,Double> adoptionFreqMap = new HashMap<ITrait,Double>();

        for(ITrait trait: adoptionCountMap.keySet()) {
            double freq = (double) adoptionCountMap.get(trait) / (double) agentCountForTag;

            log.debug("count: " + adoptionCountMap.get(trait) + " total: " + agentCountForTag + " freq: " + freq);

            adoptionFreqMap.put(trait,freq);
        }

        return adoptionFreqMap;
    }


    public void removeTrait(ITrait traitToRemove) {
        this.traitMap.remove(traitToRemove);
    }
}
