/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
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
import org.madsenlab.sim.tf.observers.TraitStatistic;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 1, 2010
 * Time: 2:19:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class UnstructuredTraitDimension<T> implements ITraitDimension<T> {
    private List<IStatisticsObserver> observers;
    String dimensionName = null;
    Map<T, ITrait<T>> traitMap = null;
    List<ITrait<T>> traitList = null;
    Logger log;
    private ISimulationModel model;
    private ITraitFactory traitFactory;

    @Inject
    public void setSimulationModel(ISimulationModel m) {
        model = m;
        log = model.getModelLogger(this.getClass());
    }

    public String toString() {
        return this.dimensionName;
    }

    @Override
    public void setTraitVariationModel(ITraitFactory f) {
        this.traitFactory = f;
    }

    public UnstructuredTraitDimension() {
        this.initialize();
    }

    public UnstructuredTraitDimension(ISimulationModel m) {
        this.setSimulationModel(m);
        this.initialize();
    }

    public UnstructuredTraitDimension(ISimulationModel m, ITraitFactory f) {
        this.setSimulationModel(m);
        this.setTraitVariationModel(f);
        this.initialize();
    }

    private void initialize() {
        //log.debug("initializing trait dimension");
        // We keep both a hash and a list; normally we get things via mapping, but
        // when we want a random trait it's more efficient to have a list than to make a new
        // temporary list every time we want to get a random trait.  We don't add and remove traits
        // very often, so the performance hit is very low
        this.traitMap = new HashMap<T, ITrait<T>>();
        this.traitList = new ArrayList<ITrait<T>>();
        this.observers = Collections.synchronizedList(new ArrayList<IStatisticsObserver>());
        this.dimensionName = "UNNAMED";

    }

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    @Override
    public ITrait<T> getNewVariantWithSpecifiedValue(T val) {
        ITrait<T> newTrait = new UnstructuredTrait<T>(this.model);
        newTrait.setTraitID(val);
        log.debug("Creating new trait with value: " + String.valueOf(val));
        newTrait.setOwningDimension(this);
        this.addTrait(newTrait);
        return newTrait;
    }

    private synchronized void addTrait(ITrait<T> newTrait) {
        this.traitMap.put(newTrait.getTraitID(), newTrait);
        this.traitList.add(newTrait);
        for (IStatisticsObserver<ITraitDimension> obs : this.model.getObserverList()) {
            newTrait.attach(obs);
            newTrait.setOwningDimension(this);
        }
    }

    public ITrait<T> getTrait(T traitID) {
        // only needs to use the map
        return this.traitMap.get(traitID);
    }

    public Collection<ITrait<T>> getTraitsInDimension() {
        // only needs to use the map
        return this.traitMap.values();
    }

    /**
     * Returns a random trait from the dimension.  CAUTION:  This returns ANY trait in the dimension,
     * even one which has a zero adoption count.  It is useful for initializing a population, or when
     * you want innovation to "resurrect" extinct traits.  But it should not be used to select a *current*
     * trait randomly with a non-zero adoption count.
     *
     * @return
     */

    public ITrait<T> getRandomTraitFromDimension() {
        // needs to use the list only
        Integer randomTraitNumber = this.model.getUniformRandomInteger(this.traitList.size() - 1);
        return this.traitList.get(randomTraitNumber);
    }

    public Map<ITrait<T>, Integer> getCurGlobalTraitCounts() {
        Map<ITrait<T>, Integer> countMap = new HashMap<ITrait<T>, Integer>();

        //for (ITrait trait : this.traitMap.values()) {
        for (ITrait trait : this.traitList) {
            if (trait.getCurrentAdoptionCount() > 0) {
                countMap.put(trait, trait.getCurrentAdoptionCount());
            }
        }
        return countMap;
    }

    public Map<ITrait<T>, Double> getCurGlobalTraitFrequencies() {
        log.trace("getting current trait frequency map");
        Map<ITrait<T>, Double> freqMap = new HashMap<ITrait<T>, Double>();
        Integer total = 0;

        Integer popsize = this.model.getPopulation().getCurrentPopulationSize();
        if (popsize == 0) {
            throw new IllegalStateException("getCurGlobalTraitFrequencies called with empty population");
        }

        for (ITrait<T> trait : this.traitList) {
            if (trait.getCurrentAdoptionCount() > 0) {
                double freq = (double) trait.getCurrentAdoptionCount() / (double) popsize;
                freqMap.put(trait, freq);
            }
        }
        return freqMap;
    }

    public Map<ITrait<T>, Integer> getCurTraitCountByTag(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Getting trait counts by tag requires a non-null reference to an IAgentTag object");
        log.debug("Entering getCurTraitCountByTag for tag: " + tag);

        // We use a Set to gather a unique list of the traits held by all
        // agents tagged with the tag specified.    
        Set<ITrait<T>> traitsAdopted = new HashSet<ITrait<T>>();
        List<IAgent> agentList = tag.getCurAgentsTagged();

        log.debug("agents tagged: " + agentList.size());


        for (IAgent agent : agentList) {
            Set<ITrait> myTraits = agent.getCurrentlyAdoptedTraits();

            for (ITrait<T> aTrait : myTraits) {
                //log.debug("trait added to traitsAdopted: " + aTrait);
                traitsAdopted.add(aTrait);
            }
        }

        log.debug("traitsAdopted size: " + traitsAdopted.size());

        // Now we go through and ask each trait its adoption count for
        // this tag
        Map<ITrait<T>, Integer> adoptionCountMap = new HashMap<ITrait<T>, Integer>();
        for (ITrait<T> trait : traitsAdopted) {

            Integer count = trait.getCurrentAdoptionCountForTag(tag);
            log.debug("trait: " + trait + " count: " + count);
            adoptionCountMap.put(trait, count);
        }


        log.debug("Exiting getCurTraitCountByTag");
        return adoptionCountMap;
    }


    public Map<ITrait<T>, Double> getCurTraitFreqByTag(IAgentTag tag) {
        Preconditions.checkNotNull(tag, "Getting trait frequencies by tag requires a non-null reference to an IAgentTag object");
        Integer agentCountForTag = tag.curAgentCount();

        Map<ITrait<T>, Integer> adoptionCountMap = this.getCurTraitCountByTag(tag);
        log.trace("adoptionCountMap: " + adoptionCountMap);
        Map<ITrait<T>, Double> adoptionFreqMap = new HashMap<ITrait<T>, Double>();

        for (ITrait<T> trait : adoptionCountMap.keySet()) {
            if (agentCountForTag == 0) {
                adoptionFreqMap.put(trait, 0.0);
            } else {
                double freq = (double) adoptionCountMap.get(trait) / (double) agentCountForTag;
                log.debug("count: " + adoptionCountMap.get(trait) + " total: " + agentCountForTag + " freq: " + freq);
                adoptionFreqMap.put(trait, freq);
            }
        }

        return adoptionFreqMap;
    }


    private synchronized void removeTrait(ITrait<T> traitToRemove) {
        this.traitMap.remove(traitToRemove);
        this.traitList.remove(traitToRemove);
    }

    public void attach(IStatisticsObserver obs) {
        synchronized (this.observers) {
            log.trace("attaching to obs: " + obs);
            this.observers.add(obs);
        }
    }

    public void attach(List<IStatisticsObserver> obsList) {
        for (IStatisticsObserver obs : obsList) {
            this.attach(obs);
        }
    }

    public void detach(IStatisticsObserver obs) {
        synchronized (this.observers) {
            this.observers.remove(obs);
        }

    }

    public void detach(List<IStatisticsObserver> obsList) {
        for (IStatisticsObserver obs : obsList) {
            this.detach(obs);
        }
    }

    public Integer getNumObservers() {
        return this.observers.size();
    }

    public void notifyObservers() {
        IStatistic stat = this.getChangeStatistic();
        log.debug("change statistic: " + stat);
        for (IStatisticsObserver obs : this.observers) {
            log.debug("notify observer: " + obs);
            obs.updateStatistics(stat);
        }
    }

    public IStatistic getChangeStatistic() {
        return new TraitStatistic(this, model.getCurrentModelTime());
    }

    @Override
    public ITrait getNewUniqueUniformVariant() {
        ITrait newTrait = this.traitFactory.getNewUniqueUniformVariant();
        newTrait.setOwningDimension(this);
        this.addTrait(newTrait);
        return newTrait;
    }

    @Override
    public ITrait getNewVariantBasedUponExistingVariant(ITrait existingTrait) {
        ITrait newTrait = this.traitFactory.getNewVariantBasedUponExistingVariant(existingTrait);
        newTrait.setOwningDimension(this);
        this.addTrait(newTrait);
        return newTrait;
    }

    @Override
    public Boolean providesInfiniteVariants() {
        return this.traitFactory.providesInfiniteVariants();
    }

    @Override
    public Set<ITrait> getUniqueUniformTraitCollection(Integer numTraits) {
        Set<ITrait> traits = this.traitFactory.getUniqueUniformTraitCollection(numTraits);
        this.trackNewTraits(traits);
        return traits;
    }

    @Override
    public Set<ITrait> getGaussianTraitCollection(Integer numTraits, Double mean, Double stdev) {
        Set<ITrait> traits = this.traitFactory.getGaussianTraitCollection(numTraits, mean, stdev);
        this.trackNewTraits(traits);
        return traits;
    }

    @Override
    public Set<ITrait> getUniformTraitCollection(Integer numTraits) {
        Set<ITrait> traits = traitFactory.getUniformTraitCollection(numTraits);
        this.trackNewTraits(traits);
        return traits;
    }

    private void trackNewTraits(Set<ITrait> traits) {
        for (ITrait trait : traits) {
            trait.setOwningDimension(this);
            this.addTrait(trait);
        }
    }
}
