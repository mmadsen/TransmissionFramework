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
import org.madsenlab.sim.tf.classification.UnitIntervalClassDimension;
import org.madsenlab.sim.tf.classification.UnstructuredClassParadigmaticClassification;
import org.madsenlab.sim.tf.config.*;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.interfaces.classification.IClassDimension;
import org.madsenlab.sim.tf.interfaces.classification.IClassification;
import org.madsenlab.sim.tf.traits.UnstructuredTraitDimensionFactory;
import org.madsenlab.sim.tf.enums.ClassDimensionModeType;
import org.madsenlab.sim.tf.enums.ObserverTargetType;
import org.madsenlab.sim.tf.utils.TraitPredicate;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurableSimulationModel implements ISimulationModel {
    protected Logger log;
    protected Integer currentTime = 0;
    @Inject
    protected
    Provider<IAgent> agentProvider;
    @Inject
    protected
    Provider<ITrait> traitProvider;

    @Inject
    protected Provider<IPopulation> populationProvider;
    @Inject
    protected Provider<IDeme> demeProvider;

    @Inject
    protected ILogFiles logFileHandler;

    protected IInteractionTopology topology;

    protected RandomEngine rngGenerator;
    protected RandomEngine rngGeneratorNormal;
    protected RandomEngine rngGeneratorExp;
    protected Uniform uniformDist;
    protected Normal normalDist;
    protected Exponential expDist;

    protected IPopulation agentPopulation;
    protected Integer lengthSimulation;
    protected List<ITraitDimension> dimensionList;
    protected Map<Integer, ITraitDimension> dimensionMap;
    protected List<IActionRule> rulesetList;
    protected Map<Integer, IActionRule> rulesetMap;

    protected List<IStatisticsObserver> traitObserverList;
    protected List<IStatisticsObserver> classObserverList;

    protected Set<IClassification> classificationSet;
    protected String propertiesFileName;
    protected String modelNamePrefix;

    protected IModelDynamics modelDynamicsDelegate;
    protected IInitialPopulationBuilder initialPopulationBuilder;
    protected ModelConfiguration modelConfig;
    protected IInitialPopulationBuilder populationBuilder;
    protected String loggingDirectoryRoot;

    public ConfigurableSimulationModel() {

    }

    public void initializeConfigurationAndLoggingFromProperties() {
        this.logFileHandler.initializeLogFileHandler(this.loggingDirectoryRoot);
        String loggingDirectory = this.logFileHandler.getLoggingDirectory();
        System.out.println("logging directory: " + loggingDirectory);
        System.setProperty("log4j.logpath", loggingDirectory);

        String test = System.getProperty("log4j.logpath");

        log = Logger.getLogger(this.getClass());
        log.info("log4j configured and ready in directory: " + loggingDirectory);
        log.info("testing system property: " + test);
    }

    public IPopulation getPopulation() {
        return this.agentPopulation;
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
        this.agentPopulation = populationProvider.get();
        this.dimensionList = new ArrayList<ITraitDimension>();
        this.traitObserverList = new ArrayList<IStatisticsObserver>();
        this.classObserverList = new ArrayList<IStatisticsObserver>();
        this.dimensionMap = new HashMap<Integer, ITraitDimension>();
        this.classificationSet = new HashSet<IClassification>();
        this.rulesetList = new ArrayList<IActionRule>();
        this.rulesetMap = new HashMap<Integer, IActionRule>();
    }

    public Provider<ITrait> getTraitProvider() {
        return traitProvider;
    }

    public Provider<ITraitDimension> getTraitDimensionProvider() {
        return null;
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

    public ModelConfiguration getModelConfiguration() {
        return this.modelConfig;
    }

    public void run() {
        log.info("Beginning simulation run");
        int tenpercent = this.modelConfig.getSimlength() / 10;
        while (this.currentTime < this.modelConfig.getSimlength()) {
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
        for (IStatisticsObserver<IClassification> cObs : this.classObserverList) {
            cObs.endSimulationAction();
            cObs.finalizeObservation();
        }
        log.info("Finalizing model run, writing historical data, and closing any files or connections");
    }

    public void modelObservations() {
        log.trace("entering modelObservations at time: " + this.currentTime);
        for (IStatisticsObserver<ITraitDimension> obs : this.traitObserverList) {
            obs.perStepAction();
        }
        for (IStatisticsObserver<IClassification> cObs : this.classObserverList) {
            cObs.perStepAction();
        }
    }

    public void parseCommandLineOptions(String[] args) {

        Options cliOptions = new Options();
        cliOptions.addOption("c", true, "path to configuration file");
        cliOptions.addOption("p", true, "path to directory root for simulation output");

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.out.println("ERROR: Command line exception: " + ex.toString());
            System.exit(1);
        }

        if (cmd.hasOption("p")) {
            this.loggingDirectoryRoot = cmd.getOptionValue("p");
            // THIS MUST BE DONE BEFORE WE CAN USE APACHE CONFIGURATION TO PARSE THE CONFIGURATION FILE
            this.initializeConfigurationAndLoggingFromProperties();
        } else {
            System.out.println("ERROR: need to specify a logging root directory using the -p option");
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

        this.modelConfig.setLogParentDirectory(this.loggingDirectoryRoot);


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
            log.error("Fatal exception loading model dynamics class: " + ex.getMessage());
            System.exit(1);
        }

        this.modelConfig.setGenerationDynamicsMode(this.modelDynamicsDelegate.getGenerationDynamicsMode());

        String topologyClass = this.modelConfig.getPopulation().getTopologyclass();

        try {
            Class<?> clazz = Class.forName(topologyClass);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            this.topology = (IInteractionTopology) constructor.newInstance(this);
        } catch (Exception ex) {
            log.error("Fatal exception loading topology class: " + ex.getMessage());
            System.exit(1);
        }


        // Initialize a population builder object
        String populationBuilderDelegate = this.modelConfig.getPopulation().getBuilderclass();

        //  initialize trait dimensions
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


        // initialize classifications, linked to trait dimensions
        List<ClassificationConfiguration> classConfigList = this.modelConfig.getClassificationConfigurations();
        for (ClassificationConfiguration classConfig : classConfigList) {
            String classificationName = classConfig.getClassificationName();
            IClassification classification = new UnstructuredClassParadigmaticClassification(this);
            classification.setClassificationName(classificationName);

            List<ClassificationDimensionConfiguration> classDimConfigList = classConfig.getClassificationDimensionConfigurations();
            for (ClassificationDimensionConfiguration classDimConfig : classDimConfigList) {
                Integer dimensionTrackedID = classDimConfig.getTraitDimensionTracked();
                ITraitDimension traitDimension = this.dimensionMap.get(dimensionTrackedID);
                IClassDimension classDimension = new UnitIntervalClassDimension<Double>(this, traitDimension);

                ClassDimensionModeType modeType = classDimConfig.getModeType();
                if (modeType.equals(ClassDimensionModeType.RANDOM)) {
                    Integer numModes = classDimConfig.getNumberModesForRandomModeType();
                    classDimension.createRandomModeSet(numModes);
                } else if (modeType.equals(ClassDimensionModeType.SPECIFIED)) {
                    Set<TraitPredicate> predSet = classDimConfig.getModePredicates();
                    classDimension.createSpecifiedModeSet(predSet);
                } else {
                    log.error("unknown configuration element: " + modeType.toString());
                    System.exit(1);
                }

                classification.addClassDimension(classDimension);

            }
            classification.initializeClasses();
            this.classificationSet.add(classification);


        }

        // Initialize observers

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
            } else {
                log.error("unknown configuration element: " + targetType.toString());
                System.exit(1);
            }


        }


        // Initialize trait dimensions and classifications with their respective observers
        for (ITraitDimension dimension : this.dimensionList) {
            dimension.attach(this.traitObserverList);
        }

        for (IClassification classification : this.classificationSet) {
            classification.attach(this.classObserverList);
        }


        // TODO - initialize agent population with initial trait variants and rulesets
        List<RulesetConfiguration> rulesetConfigurations = this.modelConfig.getPopulation().getRulesets();
        for (RulesetConfiguration rulesetConfig : rulesetConfigurations) {
            IActionRule rule = this.instantiateRuleset(rulesetConfig);
            this.rulesetMap.put(rulesetConfig.getRulesetID(), rule);
            this.rulesetList.add(rule);
        }

        // Initialize a population builder
        String builderClass = this.modelConfig.getPopulation().getBuilderclass();
        try {
            Class<?> clazz = Class.forName(builderClass);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            this.populationBuilder = (IInitialPopulationBuilder) constructor.newInstance(this);
        } catch (Exception ex) {
            log.error("Fatal exception loading population builder: " + ex.getMessage());
            System.exit(1);
        }

        this.populationBuilder.setModelConfiguration(this.getModelConfiguration());
        this.populationBuilder.setRulesets(this.rulesetMap);
        IPopulation initialPop = this.populationProvider.get();
        this.agentPopulation = this.populationBuilder.constructInitialPopulation(initialPop, this.dimensionMap);

        // The simulation model is initialized and ready to run.
        log.info("Initial population constructed and model ready to run");

        log.debug("exiting initializeModel");
    }

    private IActionRule instantiateRuleset(RulesetConfiguration config) {
        // First run through the ruleset, instantiate each rule, configuring
        // Then we go through sequentially and "thread" the subrules together, passing back the final chain
        Map<Integer, IActionRule> tempRuleMap = new HashMap<Integer, IActionRule>();
        Map<Integer, Integer> subruleMap = new HashMap<Integer, Integer>();

        for (RuleConfiguration ruleConfig : config.getRuleList()) {
            String ruleClass = ruleConfig.getRuleClass();
            Integer ruleID = ruleConfig.getRuleID();
            Integer subruleOf = ruleConfig.getSubruleOf();
            subruleMap.put(ruleID, subruleOf);
            IActionRule ruleObj = null;

            try {
                Class<?> clazz = Class.forName(ruleClass);
                Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
                ruleObj = (IActionRule) constructor.newInstance(this);
            } catch (Exception ex) {
                log.error("Fatal exception loading population builder: " + ex.getMessage());
                System.exit(1);
            }

            Map<String, String> parameters = ruleConfig.getParameters();
            ruleObj.setParameters(parameters);
            tempRuleMap.put(ruleID, ruleObj);
        }

        // now thread the subrules into the main rule
        IActionRule mainRule = tempRuleMap.get(1);
        for (Integer ruleID : subruleMap.keySet()) {
            if (ruleID.equals(1)) {
                // do nothing
            } else {
                Integer subruleOf = subruleMap.get(ruleID);
                IActionRule upperRule = tempRuleMap.get(subruleOf);
                IActionRule lowerRule = tempRuleMap.get(ruleID);
                upperRule.registerSubRule(lowerRule);
            }
        }

        return mainRule;
    }


    private ITraitFactory instantiateTraitFactory(String traitFactoryClassName) {
        ITraitFactory traitFactory = null;
        try {
            Class<?> clazz = Class.forName(traitFactoryClassName);
            Constructor<?> constructor = clazz.getConstructor(ISimulationModel.class);
            traitFactory = (ITraitFactory) constructor.newInstance(this);
        } catch (Exception ex) {
            log.error("Fatal exception finding trait factory class: " + ex.getMessage());
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
            log.error("Fatal exception finding observer class: " + observerClassName + "  msg: " + ex.getMessage());
            //ex.printStackTrace();
            System.exit(1);
        }
        log.info("instantiated observer: " + observer);
        return observer;
    }

    public void debugCheckInitialPopulation() {
        List<IAgent> agentList = this.agentPopulation.getAgents();
        Map<String, Integer> traitCounts = new HashMap<String, Integer>();
        for (IAgent agent : agentList) {
            Map<ITraitDimension, ITrait> traitDimensionMap = agent.getCurrentlyAdoptedDimensionsAndTraits();
            log.info("agent " + agent.getAgentID() + " has traits: " + traitDimensionMap);


            Set<ITrait> traitList = agent.getCurrentlyAdoptedTraits();
            for (ITrait trait : traitList) {
                Integer cnt = traitCounts.get(trait.getTraitID());
                if (cnt == null) {
                    traitCounts.put(trait.getTraitID().toString(), 1);
                } else {
                    cnt++;
                    traitCounts.put(trait.getTraitID().toString(), cnt);
                }
            }
        }
        Set<String> traitSet = traitCounts.keySet();
        ArrayList<String> sortedTraits = new ArrayList<String>(traitSet);
        Collections.sort(sortedTraits);

        log.info("====================================================================");
        log.info("agent population size: " + agentList.size());
        for (String trait : sortedTraits) {
            System.out.println(trait + "\t" + traitCounts.get(trait));
        }
        log.info("number of traits found: " + traitCounts.size());
        log.info("====================================================================");
    }

    @Override
    public Set<IClassification> getClassificationSet() {
        return classificationSet;
    }
}
