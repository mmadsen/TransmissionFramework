/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.traits;

import com.google.inject.Inject;
import com.google.inject.internal.Preconditions;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.*;

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
    Map<String, ITrait> traitMap = null;
    List<ITrait> traitList = null;
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
        // We keep both a hash and a list; normally we get things via mapping, but
        // when we want a random trait it's more efficient to have a list than to make a new
        // temporary list every time we want to get a random trait.  We don't add and remove traits
        // very often, so the performance hit is very low
        this.traitMap = new HashMap<String, ITrait>();
        this.traitList = new ArrayList<ITrait>();

    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public synchronized void addTrait(ITrait newTrait) {
        this.traitMap.put(newTrait.getTraitID(), newTrait);
        this.traitList.add(newTrait);
    }

    public ITrait getTrait(String traitID) {
        // only needs to use the map
        return this.traitMap.get(traitID);
    }

    public Collection<ITrait> getTraitsInDimension() {
        // only needs to use the map
        return this.traitMap.values();
    }

    public ITrait getRandomTraitFromDimension() {
        // needs to use the list only
        Integer randomTraitNumber = this.model.getUniformRandomInteger(this.traitList.size()-1);
        return this.traitList.get(randomTraitNumber);
    }

    public Map<ITrait, Integer> getCurGlobalTraitCounts() {
        Map<ITrait, Integer> countMap = new HashMap<ITrait, Integer>();

        for (ITrait trait : this.traitMap.values()) {
            countMap.put(trait, trait.getCurrentAdoptionCount());
        }
        return countMap;
    }

    public Map<ITrait, Double> getCurGlobalTraitFrequencies() {
        log.trace("getting current trait frequency map");
        Map<ITrait, Double> freqMap = new HashMap<ITrait, Double>();
        Integer total = 0;

        Integer popsize = this.model.getPopulation().getCurrentPopulationSize();
        if (popsize == 0) {
            throw new IllegalStateException("getCurGlobalTraitFrequencies called with empty population");
        }

        for (ITrait trait : this.traitMap.values()) {
            double freq = (double) trait.getCurrentAdoptionCount() / (double) popsize;
            freqMap.put(trait, freq);
        }
        return freqMap;
    }

    public Map<ITrait, Integer> getCurTraitCountByTag(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Getting trait counts by tag requires a non-null reference to an IAgentTag object");
        log.debug("Entering getCurTraitCountByTag for tag: " + tag);

        // We use a Set to gather a unique list of the traits held by all
        // agents tagged with the tag specified.    
        Set<ITrait> traitsAdopted = new HashSet<ITrait>();
        List<IAgent> agentList = tag.getCurAgentsTagged();

        log.debug("agents tagged: " + agentList.size());


        for (IAgent agent : agentList) {
            Set<ITrait> myTraits = agent.getCurrentlyAdoptedTraits();

            for (ITrait aTrait : myTraits) {
                //log.debug("trait added to traitsAdopted: " + aTrait);
                traitsAdopted.add(aTrait);
            }
        }

        log.debug("traitsAdopted size: " + traitsAdopted.size());

        // Now we go through and ask each trait its adoption count for
        // this tag
        Map<ITrait, Integer> adoptionCountMap = new HashMap<ITrait, Integer>();
        for (ITrait trait : traitsAdopted) {

            Integer count = trait.getCurrentAdoptionCountForTag(tag);
            log.debug("trait: " + trait + " count: " + count);
            adoptionCountMap.put(trait, count);
        }


        log.debug("Exiting getCurTraitCountByTag");
        return adoptionCountMap;
    }


    public Map<ITrait, Double> getCurTraitFreqByTag(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Getting trait frequencies by tag requires a non-null reference to an IAgentTag object");
        Integer agentCountForTag = tag.curAgentCount();

        Map<ITrait, Integer> adoptionCountMap = this.getCurTraitCountByTag(tag);
        log.trace("adoptionCountMap: " + adoptionCountMap);
        Map<ITrait, Double> adoptionFreqMap = new HashMap<ITrait, Double>();

        for (ITrait trait : adoptionCountMap.keySet()) {
            if(agentCountForTag == 0) {
                adoptionFreqMap.put(trait,0.0);
            }
            else {
                double freq = (double) adoptionCountMap.get(trait) / (double) agentCountForTag;
                log.debug("count: " + adoptionCountMap.get(trait) + " total: " + agentCountForTag + " freq: " + freq);
                adoptionFreqMap.put(trait, freq);
            }
        }

        return adoptionFreqMap;
    }


    public synchronized void removeTrait(ITrait traitToRemove) {
        this.traitMap.remove(traitToRemove);
        this.traitList.remove(traitToRemove);
    }
}
