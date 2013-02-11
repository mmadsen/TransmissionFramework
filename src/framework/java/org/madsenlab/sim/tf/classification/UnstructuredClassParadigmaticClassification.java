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
import org.madsenlab.sim.tf.analysis.ClassificationStatistic;
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


    public UnstructuredClassParadigmaticClassification(ISimulationModel model) {
        this.classDimensionSet = new HashSet<IClassDimension>();
        this.classSet = new HashSet<IClass>();
        this.classObserverList = new ArrayList<IStatisticsObserver>();
        this.memoClassIdentificationForAgent = new HashMap<IAgent, IClass>();
        this.indexModesToClass = new HashMap<Integer, IClass>();
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
    }

    @Override
    public void initializeClasses() {
        // Precondition is that the classDimensionSet.size() > 1

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
            IClass newClass = new UnstructuredClass(modeList);
            log.debug("Creating class: " + newClass.getClassDescription());
            this.classSet.add(newClass);
            this.indexModesToClass.put(newClass.getUniqueID(), newClass);

        }
    }

    @Override
    public Set<IClass> getClasses() {
        Preconditions.checkNotNull(this.classSet, "You must initializeClasses() before calling this method");
        return this.classSet;
    }

    // TODO:  class counts
    @Override
    public Map<IClass, Integer> getCurGlobalClassCounts() {
        return null;
    }

    // TODO:  class counts by tag
    @Override
    public Map<IClass, Integer> getCurClassCountByTag(IAgentTag tag) {
        return null;
    }

    // TODO:  class freq
    @Override
    public Map<IClass, Double> getCurGlobalClassFrequencies() {
        return null;
    }

    // TODO:  class freq by tag
    @Override
    public Map<IClass, Double> getCurClassFreqByTag(IAgentTag tag) {
        return null;
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


    /* Methods deriving from IClassIdentificationEngine */

    @Override
    public IClass getClassForAgent(IAgent agent) {
        log.trace("Entering getClassForAgent");
        // calls getClassForTraits() for the current traits held by an agent
        // by determining the ITraitDimension for each trait,
        // then figuring out which IClassDimension corresponds to a given ITraitDimension,
        // asking the IClassDimension which IClassDimensionMode corresponds to a given ITrait value
        // and then asking the classification for the IClass which is indexed to a given list of modes.


        log.trace("Exiting getClassForAgent");
        return null;
    }

    @Override
    public IClass getClassForTraits(Set<ITrait> traitSet) {
        return null;
    }

    @Override
    public void updateClassForAgent(IAgent agent) {
        log.trace("Entering updateClassForAgent");
        // calls getClassForAgent() to get the current class given adopted traits
        // checks to see if the current class has changed from previous step
        // if so, does class unadopt/adopt
        IClass currentClass = this.getClassForAgent(agent);
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
}
