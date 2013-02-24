/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.classification;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.observers.ClassificationStatistic;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.interfaces.classification.*;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 1:56 PM
 */

public class UnstructuredClassParadigmaticClassification implements IClassification, IClassIdentificationEngine {
    private Set<IClassDimension> classDimensionSet;
    private Set<IClass> classSet = null;
    private List<IStatisticsObserver> classObserverList;
    private ISimulationModel model;
    private Logger log;
    private Map<IAgent, IClass> memoClassIdentificationForAgent;
    private Map<Integer, IClass> indexModesToClass;
    private Map<ITraitDimension, IClassDimension> indexTraitDimensionsToClassDimension;
    private Integer highestClassIDWithinClassification = 0;


    public UnstructuredClassParadigmaticClassification(ISimulationModel model) {
        this.classDimensionSet = new HashSet<IClassDimension>();
        this.classSet = new HashSet<IClass>();
        this.classObserverList = new ArrayList<IStatisticsObserver>();
        this.memoClassIdentificationForAgent = new HashMap<IAgent, IClass>();
        this.indexModesToClass = new HashMap<Integer, IClass>();
        this.indexTraitDimensionsToClassDimension = new HashMap<ITraitDimension, IClassDimension>();
        this.model = model;
        log = model.getModelLogger(this.getClass());
    }


    @Override
    public Set<IClassDimension> getClassificationDimensions() {
        Preconditions.checkNotNull(this.classSet, "You must initializeClasses() before calling this method");
        return classDimensionSet;
    }

    @Override
    public Integer getNumClasses() {
        Preconditions.checkNotNull(this.classSet, "You must initializeClasses() before calling this method");
        return classSet.size();
    }

    @Override
    public IClassIdentificationEngine getClassIdentifier() {
        return this;
    }

    @Override
    public void addClassDimension(IClassDimension dim) {
        this.classDimensionSet.add(dim);
        ITraitDimension traitDim = dim.getTrackedTraitDimension();
        this.indexTraitDimensionsToClassDimension.put(traitDim, dim);
    }

    @Override
    public void initializeClasses() {

        // Make an array of sets of modes
        int numDimensions = this.classDimensionSet.size();
        log.debug("Creating classification from " + numDimensions + " dimensions");
        Set[] dimensionModeSets = new Set[numDimensions];
        int i = 0;
        for (IClassDimension dimension : this.classDimensionSet) {
            dimensionModeSets[i] = dimension.getModeSet();
            i++;
        }

        // Get the cartesian product of all the modes
        Set<List<IClassDimensionMode>> setAllModeCombinations = Sets.cartesianProduct(dimensionModeSets);
        log.debug("Created cartesian product of all class dimension modes, yielding " + setAllModeCombinations.size() + " classes");
        // For each element of the cartesian product, create an IClass object, add to the classSet
        // Register the IClass object into the index of hashcodes, so that we can look up a class by a list of its modes.
        for (List<IClassDimensionMode> modeList : setAllModeCombinations) {
            this.highestClassIDWithinClassification++;
            IClass newClass = new UnstructuredClass(this.model, modeList, this.highestClassIDWithinClassification);
            log.debug("Creating class: " + this.highestClassIDWithinClassification + " description: " + newClass.getClassDescription() + " with mode hashcode: " + newClass.getUniqueID());
            this.classSet.add(newClass);
            this.indexModesToClass.put(newClass.getUniqueID(), newClass);
        }
    }

    @Override
    public Set<IClass> getClasses() {
        Preconditions.checkNotNull(this.classSet, "You must initializeClasses() before calling this method");
        return this.classSet;
    }

    @Override
    public Map<Integer, IClass> getTableClassIDandClass() {
        Map<Integer, IClass> classMap = new HashMap<Integer, IClass>();
        for (IClass cz : this.classSet) {
            classMap.put(cz.getClassIDWithinClassification(), cz);
        }
        return classMap;
    }

    @Override
    public Map<Integer, String> getTableClassIDandDescription() {
        Map<Integer, String> classMap = new HashMap<Integer, String>();
        for (IClass cz : this.classSet) {
            classMap.put(cz.getClassIDWithinClassification(), cz.getClassDescription());
        }
        return classMap;
    }

    @Override
    public Map<IClass, Integer> getCurGlobalClassCounts() {
        Map<IClass, Integer> countMap = new HashMap<IClass, Integer>();
        for (IClass cz : this.classSet) {
            countMap.put(cz, cz.getCurrentAdoptionCount());
        }

        return countMap;
    }

    @Override
    public Map<IClass, Integer> getCurClassCountByTag(IAgentTag tag) {
        Map<IClass, Integer> countMap = new HashMap<IClass, Integer>();
        for (IClass cz : this.classSet) {
            countMap.put(cz, cz.getCurrentAdoptionCountForTag(tag));
        }

        return countMap;
    }

    @Override
    public Map<IClass, Double> getCurGlobalClassFrequencies() {
        Map<IClass, Double> freqMap = new HashMap<IClass, Double>();

        Integer popsize = this.model.getPopulation().getCurrentPopulationSize();
        if (popsize == 0) {
            throw new IllegalStateException("getCurGlobalTraitFrequencies called with empty population");
        }

        for (IClass cz : this.classSet) {

            double freq = (double) cz.getCurrentAdoptionCount() / (double) popsize;
            freqMap.put(cz, freq);

        }
        return freqMap;
    }

    @Override
    public Map<IClass, Double> getCurClassFreqByTag(IAgentTag tag) {
        Map<IClass, Double> freqMap = new HashMap<IClass, Double>();
        int totalAdoptionCount = 0;

        // first we need to know the total number of agents

        totalAdoptionCount = tag.curAgentCount();
        log.info("total adoption count: " + totalAdoptionCount);

        for (IClass cz : this.classSet) {
            int adoptionCount = cz.getCurrentAdoptionCountForTag(tag);
            log.info("adoptionCount: " + adoptionCount);
            double freq = (double) adoptionCount / (double) totalAdoptionCount;
            freqMap.put(cz, freq);

        }

        return freqMap;
    }


    public void attach(IStatisticsObserver obs) {
        synchronized (this.classObserverList) {
            log.trace("attaching to obs: " + obs);
            this.classObserverList.add(obs);
        }
    }

    public void attach(List<IStatisticsObserver> obsList) {
        for (IStatisticsObserver obs : obsList) {
            this.attach(obs);
        }
    }

    public void detach(IStatisticsObserver obs) {
        synchronized (this.classObserverList) {
            this.classObserverList.remove(obs);
        }

    }

    public void detach(List<IStatisticsObserver> obsList) {
        for (IStatisticsObserver obs : obsList) {
            this.detach(obs);
        }
    }

    public Integer getNumObservers() {
        return this.classObserverList.size();
    }


    public void notifyObservers() {
        IStatistic stat = new ClassificationStatistic(this, model.getCurrentModelTime());
        log.debug("change statistic: " + stat);
        for (IStatisticsObserver obs : this.classObserverList) {
            log.debug("notify observer: " + obs);
            obs.updateStatistics(stat);
        }
    }

    @Override
    public void updateClassForAgent(IAgent agent) {
        log.trace("Entering updateClassForAgent");
        // calls getClassForAgent() to get the current class given adopted traits
        // checks to see if the current class has changed from previous step
        // if so, does class unadopt/adopt
        if (this.memoClassIdentificationForAgent.get(agent) == null) {
            log.debug("Initializing dummy class for agent to prime cache");
            this.memoClassIdentificationForAgent.put(agent, new DummyClass());
        }

        IClass currentClass = this.getClassForAgent(agent);
        log.debug("current class: " + currentClass.getClassDescription());
        IClass prevClass = this.memoClassIdentificationForAgent.get(agent);
        if (currentClass.equals(prevClass)) {
            log.trace("No change in class for agent: " + agent.getAgentID());
        } else {
            log.debug("Agent " + agent.getAgentID() + " changed class membership");
            log.trace("...from " + prevClass.getClassDescription() + " to " + currentClass.getClassDescription());
            // should this be protected by a synchronized block?  prob yes if we're ever doing multithreaded models...
            prevClass.unadopt(agent);
            currentClass.adopt(agent);
        }

        log.trace("Exiting updateClassForAgent");
    }

    @Override
    public void updateClassForPopulation(IPopulation pop) {
        log.trace("Entering updateClassForPopulation");
        for (IAgent agent : pop.getAgents()) {
            this.updateClassForAgent(agent);
        }
        log.trace("Exiting updateClassForPopulation");
    }


    /* Methods deriving from IClassIdentificationEngine */

    @Override
    public IClass getClassForAgent(IAgent agent) {
        log.trace("Entering getClassForAgent");
        // Get traits and dimensions held by the agent, look up which class dimensions are
        // tracking each trait dimension.  Create a map of ClassDimension -> ITrait for an agent
        Map<ITraitDimension, ITrait> traitDimensionMap = agent.getCurrentlyAdoptedDimensionsAndTraits();
        IClass identifiedClass = this.getClassForTraits(traitDimensionMap);

        log.trace("Exiting getClassForAgent");
        return identifiedClass;
    }

    @Override
    public IClass getClassForTraits(Map<ITraitDimension, ITrait> traitMap) {
        log.trace("Entering getClassForTraits");
        Map<IClassDimension, ITrait> classDimensionsToTraits = new HashMap<IClassDimension, ITrait>();
        for (ITraitDimension traitDim : traitMap.keySet()) {

            ITrait trait = traitMap.get(traitDim);
            IClassDimension classDim = this.indexTraitDimensionsToClassDimension.get(traitDim);
            log.debug("traitDim: " + traitDim.getDimensionName() + " classDim: " + classDim.getClassDimensionName() + " trait: " + trait.toString());
            classDimensionsToTraits.put(classDim, trait);
        }

        // asking the IClassDimension which IClassDimensionMode corresponds to a given ITrait value
        Set<IClassDimensionMode> modeSet = new HashSet<IClassDimensionMode>();
        for (Map.Entry<IClassDimension, ITrait> entry : classDimensionsToTraits.entrySet()) {
            IClassDimension classDim = entry.getKey();
            ITrait trait = entry.getValue();

            IClassDimensionMode mode = classDim.getModeForTraitValue(trait);

            log.debug("classDim: " + classDim + " and trait: " + trait.toString() + " maps to mode: " + mode.getModeDescription());
            modeSet.add(mode);
        }

        // and then asking the classification for the IClass which is indexed to a given list of modes.
        Integer modeSetID = modeSet.hashCode();

        IClass identifiedClass = this.indexModesToClass.get(modeSetID);

        log.debug("Identified agent to class: " + identifiedClass.getClassDescription());


        log.trace("Exiting getClassForTraits");
        return identifiedClass;
    }


    // Used for the first update a population, where there isn't any "previous" class...
    private class DummyClass implements IClass {

        public DummyClass() {

        }

        @Override
        public Integer getClassIDWithinClassification() {
            return 0;
        }

        @Override
        public Set<IClassDimensionMode> getModesDefiningClass() {
            return null;
        }

        @Override
        public String getClassDescription() {
            return null;
        }

        @Override
        public int getCurrentAdoptionCount() {
            return 0;
        }

        @Override
        public Map<IAgentTag, Integer> getCurrentAdoptionCountsByTag() {
            return null;
        }

        @Override
        public Integer getCurrentAdoptionCountForTag(IAgentTag tag) {
            return null;
        }

        @Override
        public void adopt(IAgent agent) {

        }

        @Override
        public void unadopt(IAgent agent) {

        }

        @Override
        public List<IAgent> getCurrentAdopterList() {
            return null;
        }

        @Override
        public Boolean hasCompleteDuration() {
            return null;
        }

        @Override
        public Integer getTraitDuration() {
            return null;
        }

        @Override
        public int getUniqueID() {
            return 0;
        }

        @Override
        public int compareTo(IClass otherClass) {
            return -1;
        }

        @Override
        public void attach(IStatisticsObserver obs) {

        }

        @Override
        public void attach(List<IStatisticsObserver> obsList) {

        }

        @Override
        public void detach(IStatisticsObserver obs) {

        }

        @Override
        public void detach(List<IStatisticsObserver> obsList) {

        }

        @Override
        public Integer getNumObservers() {
            return null;
        }

        @Override
        public void notifyObservers() {

        }
    }


}
