/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.models;

import cern.jet.random.Exponential;
import cern.jet.random.Normal;
import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.MersenneTwister64;
import cern.jet.random.engine.RandomEngine;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.*;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimensionFactory;
import org.madsenlab.sim.tf.utils.ObserverTargetType;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConfigurableSimulationModel implements ISimulationModel {
    protected Logger log;
    protected Integer currentTime = 0;
    @Inject
    protected
    Provider<IAgent> agentProvider;
    @Inject
    protected
    Provider<ITrait> traitProvider;
    @Inject
    protected Provider<ITraitDimension> dimensionProvider;
    @Inject
    protected Provider<IPopulation> populationProvider;
    @Inject
    protected Provider<IDeme> demeProvider;
    @Inject
    protected Provider<IInteractionTopology> topologyProvider;
    @Inject
    protected ILogFiles logFileHandler;

    protected IPopulation population;
    protected IInteractionTopology topology;

    protected RandomEngine rngGenerator;
    protected RandomEngine rngGeneratorNormal;
    protected RandomEngine rngGeneratorExp;
    protected Uniform uniformDist;
    protected Normal normalDist;
    protected Exponential expDist;

    protected IPopulation agentPopulation;
    protected Integer lengthSimulation;
    protected GlobalModelConfiguration params;
    protected Properties modelProperties;
    protected List<ITraitDimension> dimensionList;
    protected Map<Integer, ITraitDimension> dimensionMap;
    protected List<IStatisticsObserver> traitObserverList;
    protected List<IStatisticsObserver> classObserverList;
    protected String propertiesFileName;
    protected String modelNamePrefix;

    protected IModelDynamics modelDynamicsDelegate;
    protected ModelConfiguration modelConfig;

    public ConfigurableSimulationModel() {

    }

    public void initializeConfigurationAndLoggingFromProperties() {
        // load parameter file
        try {
            this.modelProperties = new Properties();
            this.modelProperties.load(new FileReader(this.propertiesFileName));
        } catch (IOException ex) {
            System.exit(1);
        }

        this.loadPropertiesToConfig();
        this.params.setProperty("model-name-prefix", modelNamePrefix);
        this.logFileHandler.initializeLogFileHandler();
        String loggingDirectory = this.logFileHandler.getLoggingDirectory();
        System.setProperty("log4j.logpath", loggingDirectory);

        log = Logger.getLogger(this.getClass());
        log.info("log4j configured and ready in directory: " + loggingDirectory);
    }

    public IPopulation getPopulation() {
        return this.population;
    }

    public IInteractionTopology getInteractionTopology() {
        return this.topology;
    }

    public Integer getUniformRandomInteger(Integer ceiling) {
        return this.uniformDist.nextIntFromTo(0, ceiling);
    }

    public Double getUniformDouble() {
        return this.uniformDist.nextDoubleFromTo(0, 1);
    }

    @Override
    public Double getStandardNormalVariate() {
        return this.normalDist.nextDouble();
    }

    @Override
    public Double getNormalVariate(double mean, double stdev) {
        return this.normalDist.nextDouble(mean, stdev);
    }

    @Override
    public Double getExponentialVariate(double lambda) {
        return expDist.nextDouble(lambda);
    }

    public Integer getCurrentModelTime() {
        return this.currentTime;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Logger getModelLogger(Class classToLog) {
        return Logger.getLogger(classToLog);
    }

    public void initializeRNG(Boolean reproducibleStream) {
        if (reproducibleStream) {
            this.rngGenerator = new MersenneTwister64();
            this.rngGeneratorNormal = new MersenneTwister64();
            this.rngGeneratorExp = new MersenneTwister64();

        } else {
            this.rngGenerator = new MersenneTwister(new java.util.Date());
            this.rngGeneratorNormal = new MersenneTwister64(new java.util.Date());
            this.rngGeneratorExp = new MersenneTwister64(new java.util.Date());
        }
        this.uniformDist = new Uniform(this.rngGenerator);
        this.normalDist = new Normal(0, 1, this.rngGeneratorNormal);
        this.expDist = new Exponential(1.0, this.rngGeneratorExp);
    }

    public void initializeProviders() {
        this.population = populationProvider.get();
        this.topology = topologyProvider.get();
        this.dimensionList = new ArrayList<ITraitDimension>();
        this.traitObserverList = new ArrayList<IStatisticsObserver>();
        this.classObserverList = new ArrayList<IStatisticsObserver>();
        this.dimensionMap = new HashMap<Integer, ITraitDimension>();
    }

    public Provider<ITrait> getTraitProvider() {
        return traitProvider;
    }

    public Provider<ITraitDimension> getTraitDimensionProvider() {
        return dimensionProvider;
    }

    public List<ITraitDimension> getTraitDimensions() {
        return this.dimensionList;
    }

    @Override
    public List<IStatisticsObserver> getObserverList() {
        return this.traitObserverList;
    }

    public Provider<IDeme> getDemeProvider() {
        return demeProvider;
    }

    public void incrementModelTime() {
        synchronized (this.currentTime) {
            this.currentTime++;
        }
    }

    public ILogFiles getLogFileHandler() {
        //log.info("caller requesting log file handler: " + this.logFileHandler);
        return this.logFileHandler;
    }

    public GlobalModelConfiguration getModelConfiguration() {
        return this.params;
    }

    public void run() {
        log.info("Beginning simulation run");
        int tenpercent = this.lengthSimulation / 10;
        while (this.currentTime < this.lengthSimulation) {
            if (this.currentTime % tenpercent == 0) {
                log.info("    Time: " + this.currentTime);
                System.gc();
            }
            this.modelStep();
            this.modelObservations();
            this.incrementModelTime();
        }

        log.info("Simulation run completed at time step: " + this.currentTime);
        this.modelFinalize();
    }

    public void modelStep() {
        this.modelDynamicsDelegate.modelStep();
    }

    public void modelFinalize() {
        for (IStatisticsObserver<ITraitDimension> obs : this.traitObserverList) {
            obs.endSimulationAction();
            obs.finalizeObservation();
        }
        log.info("Finalizing model run, writing historical data, and closing any files or connections");
    }

    public void modelObservations() {
        log.trace("entering modelObservations at time: " + this.currentTime);
        for (IStatisticsObserver<ITraitDimension> obs : this.traitObserverList) {
            obs.perStepAction();
        }
    }

    protected void loadPropertiesToConfig() {
        Set<String> propNames = this.modelProperties.stringPropertyNames();
        for (String propName : propNames) {
            this.params.setProperty(propName, this.modelProperties.getProperty(propName));
        }
    }

    public void parseCommandLineOptions(String[] args) {
        //this.log.debug("entering parseCommandLineOptions");

        Options cliOptions = new Options();
        cliOptions.addOption("c", true, "path to configuration file");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.out.println("ERROR: Command line exception: " + ex.toString());
            System.exit(1);
        }

        if (cmd.hasOption("c")) {
            String pathname = cmd.getOptionValue("c");
            SimulationConfigurationFactory configFactory = new SimulationConfigurationFactory(this);
            configFactory.processConfigurationFile(pathname);
            this.modelConfig = configFactory.getModelConfiguration();

        } else {
            System.out.println("ERROR: need to specify a configuration file using the -c option");
            System.exit(1);
        }


    }


    public void initializeModel() {
        log.debug("entering initializeModel");

        // Initialize a dynamics delegate
        String dynamicsDelegate = this.modelConfig.getDynamicsclass();
        try {
            Class<?> clazz = Class.forName(dynamicsDelegate);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            this.modelDynamicsDelegate = (IModelDynamics) constructor.newInstance(this);
        } catch (Exception ex) {
            System.out.println("Fatal exception loading model dynamics class: " + ex.getMessage());
            System.exit(1);
        }

        // TODO - initialize trait dimensions
        UnstructuredTraitDimensionFactory dimensionFactory = new UnstructuredTraitDimensionFactory(this);

        List<TraitDimensionConfiguration> dimConfigList = this.modelConfig.getTraitDimensionConfigurations();
        for (TraitDimensionConfiguration tdc : dimConfigList) {
            String dimName = tdc.getDimensionName();
            String variationClassName = tdc.getVariationModelFactoryClass();
            String dimType = tdc.getDimensionType();
            Integer dimID = tdc.getDimensionID();

            ITraitFactory traitFactory = this.instantiateTraitFactory(variationClassName);
            Class dimTypeCz = this.getClassForDimensionType(dimType);

            ITraitDimension dimension = dimensionFactory.getNewTraitDimension(dimTypeCz, traitFactory);
            dimension.setDimensionName(dimName);
            this.dimensionList.add(dimension);
            this.dimensionMap.put(dimID, dimension);
        }


        // TODO - initialize classifications, linked to trait dimensions

        // TODO - initialize rulesets

        List<ObserverConfiguration> observerConfigurations = this.modelConfig.getObserverConfigurations();
        for (ObserverConfiguration obsConfig : observerConfigurations) {
            String obsClassName = obsConfig.getObserverClass();
            ObserverTargetType targetType = obsConfig.getTargetType();

            IStatisticsObserver observer = this.instantiateObserver(obsClassName);
            Map<String, String> parameterMap = obsConfig.getParameterMap();
            if (parameterMap.size() > 0) {
                observer.setParameterMap(parameterMap);
            }

            if (targetType.equals(ObserverTargetType.TRAITDIMENSION)) {
                this.traitObserverList.add(observer);
            } else if (targetType.equals(ObserverTargetType.CLASSIFICATION)) {
                this.classObserverList.add(observer);
            }


        }


        // TODO - initialize agent population with initial trait variants  and rulesets


        log.debug("exiting initializeModel");
    }

    private ITraitFactory instantiateTraitFactory(String traitFactoryClassName) {
        ITraitFactory traitFactory = null;
        try {
            Class<?> clazz = Class.forName(traitFactoryClassName);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            traitFactory = (ITraitFactory) constructor.newInstance(this);
        } catch (Exception ex) {
            System.out.println("Fatal exception finding trait factory class: " + ex.getMessage());
            System.exit(1);
        }
        return traitFactory;
    }

    private Class getClassForDimensionType(String dimTypeStr) {
        Class dimTypeClass = null;
        try {
            dimTypeClass = Class.forName(dimTypeStr);

        } catch (Exception ex) {
            System.out.println("Fatal exception finding dimension type class: " + ex.getMessage());
            System.exit(1);
        }
        return dimTypeClass;
    }

    private IStatisticsObserver instantiateObserver(String observerClassName) {
        IStatisticsObserver observer = null;
        try {
            Class<?> clazz = Class.forName(observerClassName);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            observer = (IStatisticsObserver) constructor.newInstance(this);
        } catch (Exception ex) {
            System.out.println("Fatal exception finding observer class: " + ex.getMessage());
            System.exit(1);
        }
        return observer;
    }

}
