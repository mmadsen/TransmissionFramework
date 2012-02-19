/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.config;

import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.utils.GenerationDynamicsMode;

import java.util.HashMap;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/25/11
 * Time: 2:25 PM
 */

public class GlobalModelConfiguration {
    ISimulationModel model;
    ITraitDimension dimension;
    Double mutationRate;
    Integer numAgents;
    Boolean isInfiniteAlleles = false;
    Integer maxTraits;
    Integer startingTraits;
    Map<String, String> otherProperties;
    Integer timeStartStatistics;

    public Boolean getAntiConformist() {
        return isAntiConformist;
    }

    public void setAntiConformist(Boolean antiConformist) {
        isAntiConformist = antiConformist;
    }

    Boolean isAntiConformist = false;
    
    public Double getConformismRate() {
        return conformismRate;
    }

    public void setConformismRate(Double conformismRate) {
        this.conformismRate = conformismRate;
    }

    Double conformismRate;

    public GenerationDynamicsMode getModelRateTimeRuns() {
        return modelRateTimeRuns;
    }

    public void setModelRateTimeRuns(GenerationDynamicsMode modelRateTimeRuns) {
        this.modelRateTimeRuns = modelRateTimeRuns;
    }

    GenerationDynamicsMode modelRateTimeRuns;

    public Integer getEwensSampleSize() {
        return ewensSampleSize;
    }

    public void setEwensSampleSize(Integer ewensSampleSize) {
        this.ewensSampleSize = ewensSampleSize;
    }

    Integer ewensSampleSize;


    public Integer getNumDemes() {
        return numDemes;
    }

    public void setNumDemes(Integer numDemes) {
        this.numDemes = numDemes;
    }

    Integer numDemes;

    public Integer getLengthSimulation() {
        return lengthSimulation;
    }

    public void setLengthSimulation(Integer lengthSimulation) {
        this.lengthSimulation = lengthSimulation;
    }

    Integer lengthSimulation;


    public GlobalModelConfiguration(ISimulationModel m) {
        this.model = m;
        this.otherProperties = new HashMap<String, String>();
    }

    public Double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(Double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Integer getNumAgents() {
        return numAgents;
    }

    public void setNumAgents(Integer numAgents) {
        this.numAgents = numAgents;
    }

    public Boolean getInfiniteAlleles() {
        return isInfiniteAlleles;
    }

    public void setInfiniteAlleles(Boolean infiniteAlleles) {
        isInfiniteAlleles = infiniteAlleles;
    }

    public Integer getMaxTraits() {
        return maxTraits;
    }

    public void setMaxTraits(Integer maxTraits) {
        this.maxTraits = maxTraits;
    }

    public Integer getStartingTraits() {
        return startingTraits;
    }

    public void setStartingTraits(Integer startingTraits) {
        this.startingTraits = startingTraits;
    }

    public void setProperty(String propName, String propValue) {
        this.otherProperties.put(propName, propValue);
    }

    public String getProperty(String propName) {
        return this.otherProperties.get(propName);
    }

    public void setTimeStartStatistics(Integer t) {
        this.timeStartStatistics = t;
    }

    public Integer getTimeStartStatistics() {
        return this.timeStartStatistics;
    }
}
