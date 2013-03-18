/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.config;

import org.madsenlab.sim.tf.enums.GenerationDynamicsMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/28/13
 * Time: 2:03 PM
 */

public class ModelConfiguration {
    private int simlength;
    private int mixingtime;
    private String dynamicsclass;
    private String logParentDirectory;
    private PopulationConfiguration population;
    private List<ObserverConfiguration> observers;
    private List<TraitDimensionConfiguration> traitDimensionConfigurations;
    private Map<Integer, TraitDimensionConfiguration> dimensionIDToTraitDimensionConfigurationMap;
    private List<ClassificationConfiguration> classificationConfigurations;
    private Map<String, ObserverConfiguration> observerConfigurationMap;
    private GenerationDynamicsMode generationDynamicsMode;
    private String modelName;


    public ModelConfiguration() {
        observers = new ArrayList<ObserverConfiguration>();
        traitDimensionConfigurations = new ArrayList<TraitDimensionConfiguration>();
        dimensionIDToTraitDimensionConfigurationMap = new HashMap<Integer, TraitDimensionConfiguration>();
        classificationConfigurations = new ArrayList<ClassificationConfiguration>();
        observerConfigurationMap = new HashMap<String, ObserverConfiguration>();
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getLogParentDirectory() {
        return logParentDirectory;
    }

    public void setLogParentDirectory(String logParentDirectory) {
        this.logParentDirectory = logParentDirectory;
    }


    public int getSimlength() {
        return simlength;
    }

    public void setSimlength(int simlength) {
        this.simlength = simlength;
    }

    public int getMixingtime() {
        return mixingtime;
    }

    public void setMixingtime(int mixingtime) {
        this.mixingtime = mixingtime;
    }

    public String getDynamicsclass() {
        return dynamicsclass;
    }

    public void setDynamicsclass(String dynamicsclass) {
        this.dynamicsclass = dynamicsclass;
    }

    public PopulationConfiguration getPopulation() {
        return population;
    }

    public void setPopulation(PopulationConfiguration population) {
        this.population = population;
    }

    public void addObserverConfiguration(ObserverConfiguration obs) {
        this.observers.add(obs);
        this.observerConfigurationMap.put(obs.getObserverClass(), obs);
    }

    public List<ObserverConfiguration> getObserverConfigurations() {
        return this.observers;
    }

    public ObserverConfiguration getObserverConfigurationForClass(Class cz) {
        String className = cz.getCanonicalName();
        return this.observerConfigurationMap.get(className);
    }

    public void addTraitDimensionConfiguration(TraitDimensionConfiguration tdc) {
        this.traitDimensionConfigurations.add(tdc);
        this.dimensionIDToTraitDimensionConfigurationMap.put(tdc.getDimensionID(), tdc);
    }

    public List<TraitDimensionConfiguration> getTraitDimensionConfigurations() {
        return this.traitDimensionConfigurations;
    }

    public TraitDimensionConfiguration getTraitDimensionConfigurationForID(Integer id) {
        return this.dimensionIDToTraitDimensionConfigurationMap.get(id);
    }

    public void addClassificationConfiguration(ClassificationConfiguration cc) {
        this.classificationConfigurations.add(cc);
    }

    public List<ClassificationConfiguration> getClassificationConfigurations() {
        return this.classificationConfigurations;
    }

    public GenerationDynamicsMode getGenerationDynamicsMode() {
        return generationDynamicsMode;
    }

    public void setGenerationDynamicsMode(GenerationDynamicsMode generationDynamicsMode) {
        this.generationDynamicsMode = generationDynamicsMode;
    }


}
