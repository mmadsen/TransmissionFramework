/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.classification;

import com.google.common.base.Preconditions;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.IAgentTag;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.IStatistic;
import org.madsenlab.sim.tf.interfaces.IStatisticsObserver;
import org.madsenlab.sim.tf.interfaces.classification.IClass;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassIdentifier;
import org.madsenlab.sim.tf.interfaces.classification.IClassification;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 1:56 PM
 */

public class ParadigmaticClassification implements IClassification {
    private Set<IClassDimension> classDimensionSet;
    private Set<IClass> classSet = null;
    private List<IStatisticsObserver> classObserverList;
    private ISimulationModel model;
    private Logger log;

    public ParadigmaticClassification(ISimulationModel model) {
        this.classDimensionSet = new HashSet<IClassDimension>();
        this.classSet = new HashSet<IClass>();
        this.classObserverList = new ArrayList<IStatisticsObserver>();
        this.model = model;
        log = model.getModelLogger(this.getClass());
    }


    @Override
    public Set<IClassDimension> getClassificationDimensions() {
        return classDimensionSet;
    }

    @Override
    public Integer getNumClasses() {
        Preconditions.checkNotNull(this.classSet, "You must initializeClasses() before calling this method");
        return classSet.size();
    }

    @Override
    public IClassIdentifier getClassIdentifier() {
        return null;
    }

    @Override
    public void addClassDimension(IClassDimension dim) {
        this.classDimensionSet.add(dim);
    }

    // TODO:  class intersection and formation
    @Override
    public void initializeClasses() {

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
        IStatistic stat = new ClassStatistic(this, model.getCurrentModelTime());
        log.debug("change statistic: " + stat);
        for (IStatisticsObserver obs : this.classObserverList) {
            log.debug("notify observer: " + obs);
            obs.updateStatistics(stat);
        }
    }
}
