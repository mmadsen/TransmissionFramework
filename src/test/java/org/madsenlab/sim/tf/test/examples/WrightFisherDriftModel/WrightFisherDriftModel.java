/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.WrightFisherDriftModel;

import org.apache.commons.cli.*;
import org.madsenlab.sim.tf.analysis.*;
import org.madsenlab.sim.tf.config.GlobalModelConfiguration;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.models.AbstractSimModel;
import org.madsenlab.sim.tf.rules.CopyOrMutateDecisionRule;
import org.madsenlab.sim.tf.rules.FiniteKAllelesMutationRule;
import org.madsenlab.sim.tf.rules.InfiniteAllelesMutationRule;
import org.madsenlab.sim.tf.rules.RandomCopyNeighborSingleDimensionRule;
import org.madsenlab.sim.tf.traits.InfiniteAllelesIntegerTraitFactory;
import org.madsenlab.sim.tf.utils.GenerationDynamicsMode;
import org.madsenlab.sim.tf.utils.TraitCopyingMode;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 10:19:32 AM
 */

public class WrightFisherDriftModel extends AbstractSimModel {
    GlobalTraitCountObserver countObserver;
    GlobalTraitFrequencyObserver freqObserver;
    GlobalTraitLifetimeObserver lifetimeObserver;
    EwensSampleFullPopulationObserver ewensSampler;
    TimeAveragedTraitCountObserver taCountSampler;
    ITraitDimension dimension;
    Integer numAgents;
    Double mutationRate;
    Boolean isInfiniteAlleles = false;
    Integer maxTraits;
    Integer startingTraits;

    List<IActionRule> ruleList;

    public WrightFisherDriftModel() {
        this.modelNamePrefix = "WrightFisherDrift";
    }


    public void initializeModel() {
        // We create a single dimension, with
        //
        ITraitFactory traitFactory = null;
        if (isInfiniteAlleles == Boolean.TRUE) {
            traitFactory = new InfiniteAllelesIntegerTraitFactory(this);
        } else {
            log.error("NEED IMPLEMENTATION FOR NON INFINITE ALLELES TRAIT FACTORY!!!");
            System.exit(1);
        }
        this.dimension = this.dimensionProvider.get();
        this.dimension.setTraitVariationModel(traitFactory);
        this.dimensionList.add(this.dimension);

        // Now can initialize Observers
        this.countObserver = new GlobalTraitCountObserver(this);
        this.lifetimeObserver = new GlobalTraitLifetimeObserver(this);
        this.ewensSampler = new EwensSampleFullPopulationObserver(this);
        this.taCountSampler = new TimeAveragedTraitCountObserver(this);
        this.observerList.add(this.taCountSampler);
        this.observerList.add(this.countObserver);
        this.observerList.add(this.lifetimeObserver);
        this.observerList.add(this.ewensSampler);

        // We observe the dimension, not individual traits, in WF, so we get a single consistent picture
        // of the population at the end of a model step.
        this.dimension.attach(this.observerList);

        // set up the stack of rules, to be fired in the order given in the list
        // in this first simulation, all agents get the same rule, but this need not be the
        // case - plan for heterogeneity!!
        this.ruleList = new ArrayList<IActionRule>();
        IInteractionRule decisionRule = new CopyOrMutateDecisionRule(this);
        IInteractionRule rcmRule = new RandomCopyNeighborSingleDimensionRule(this);
        rcmRule.setTraitCopyingMode(TraitCopyingMode.PREVIOUS); // we need to do this only for Wright-Fisher style models
        decisionRule.registerSubRule(rcmRule);


        if (this.isInfiniteAlleles) {
            IInteractionRule iiMutation = new InfiniteAllelesMutationRule(this);
            decisionRule.registerSubRule(iiMutation);
        } else {
            IInteractionRule fkMutation = new FiniteKAllelesMutationRule(this);
            decisionRule.registerSubRule(fkMutation);
        }

        // Just add the decision rule, since it has two subrules
        this.ruleList.add(decisionRule);


        this.log.debug("Creating one dimension and " + this.params.getStartingTraits() + " traits to begin");
        for (Integer i = 0; i < this.params.getStartingTraits(); i++) {
            this.dimension.getNewVariant();
        }


        this.log.debug("Creating " + this.params.getNumAgents() + " agents with random starting traits");
        for (Integer i = 0; i < this.params.getNumAgents(); i++) {
            IAgent agent = this.getPopulation().createAgent();
            agent.setAgentID(i.toString());
            agent.addActionRuleList(this.ruleList);
            ITrait randomTrait = this.dimension.getRandomTraitFromDimension();
            randomTrait.adopt(agent);
        }

        // Verify proper initialization
        log.debug("Verifying proper initial frequencies =======================");
        Map<ITrait, Double> freqMap = this.dimension.getCurGlobalTraitFrequencies();
        for (Map.Entry<ITrait, Double> entry : freqMap.entrySet()) {
            log.debug("Trait " + entry.getKey().getTraitID() + " Freq: " + entry.getValue().toString());
        }
        log.debug("=============================================");

    }

    /**
     * parseCommandLineOptions will be called at the very beginning of the simulation run,
     * before logging is configured, to determine options.  Thus, we cannot use log4j to
     * document its operation.
     *
     * @param args
     */
    public void parseCommandLineOptions(String[] args) {
        //this.log.debug("entering parseCommandLineOptions");

        this.params = new GlobalModelConfiguration(this);

        Options cliOptions = new Options();
        cliOptions.addOption("n", true, "number of agents in simulation");
        cliOptions.addOption("m", true, "mutation rate in decimal form (e.g., 0.001");
        cliOptions.addOption("s", true, "starting number of traits (must not be greater than max traits value");
        cliOptions.addOption("l", true, "length of simulation in steps");
        cliOptions.addOption("p", true, "pathname of properties file giving general model configuration (e.g., log file locations)");
        cliOptions.addOption("t", true, "model time to start recording statistics (i.e., ignore initial transient");
        cliOptions.addOption("e", true, "size of sample to take for comparison to Ewens Sampling Distribution");
        cliOptions.addOption("b", false, "switch to collecting just the five biggest time averaging windows");


        // Option group for handling traits
        Option infiniteAllelesOption = new Option("i", false, "use infinite alleles model");
        Option finiteAllelesOption = new Option("f", true, "specify K alleles model (e.g., 2, 100");
        OptionGroup traitOptionGroup = new OptionGroup();
        traitOptionGroup.setRequired(true);
        traitOptionGroup.addOption(infiniteAllelesOption);
        traitOptionGroup.addOption(finiteAllelesOption);
        cliOptions.addOptionGroup(traitOptionGroup);

        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(cliOptions, args);
        } catch (ParseException ex) {
            System.out.println("ERROR: Command line exception: " + ex.toString());
            System.exit(1);
        }

        if (cmd.hasOption("i")) {
            this.params.setInfiniteAlleles(true);
            this.isInfiniteAlleles = true;
            //this.log.info("infinite alleles model selected - no maximum trait number");
            this.params.setMaxTraits(Integer.MAX_VALUE);
        } else {
            this.params.setInfiniteAlleles(false);
            this.isInfiniteAlleles = false;
            //this.log.info("finite alleles model selected");
            this.params.setMaxTraits(Integer.parseInt(cmd.getOptionValue("f", "2")));
        }

        // This is a constant for Wright Fisher models
        this.params.setModelRateTimeRuns(GenerationDynamicsMode.DISCRETE);

        this.params.setLengthSimulation(Integer.parseInt(cmd.getOptionValue("l", "10000")));
        // this is the only parameter that's directly needed in a superclass implementation
        this.lengthSimulation = this.params.getLengthSimulation();
        this.params.setNumAgents(Integer.parseInt(cmd.getOptionValue("n", "1000")));
        this.params.setMutationRate(Double.parseDouble(cmd.getOptionValue("m", "0.000001")));
        this.params.setStartingTraits(Integer.parseInt(cmd.getOptionValue("s", "2")));
        this.propertiesFileName = cmd.getOptionValue("p", "tf-configuration.properties");
        this.params.setTimeStartStatistics(Integer.parseInt(cmd.getOptionValue("t", "100")));
        this.params.setEwensSampleSize(Integer.parseInt(cmd.getOptionValue("e", "50")));

        if (cmd.hasOption("b")) {
            this.params.setCollectLongTAWindowsOnly(true);
        } else {
            this.params.setCollectLongTAWindowsOnly(false);
        }

        // Report starting parameters
        //this.log.info("starting number of traits: " + this.params.getStartingTraits());
        //this.log.info("maximum number of traits: " + this.params.getMaxTraits());
        //this.log.info("number of agents: " + this.params.getNumAgents());
        //this.log.info("mutation rate: " + this.params.getMutationRate());
        //this.log.info("simulation length: " + this.params.getLengthSimulation());
        //this.log.info("properties file: " + this.propertiesFileName);

        //this.log.trace("exiting parseCommandLineOptions");
    }

    public void modelStep() {
        log.trace("========================== STEP: " + this.currentTime + "============================");

        // In order to ensure that we do not create numerical artifacts by using a fixed order of enumeration
        // we shuffle the population before we step through it.
        List<IAgent> shuffledAgentList = this.getPopulation().getAgentsShuffledOrder();

        // In WF model, each of the N copying events in an elemental step must be done with replacement
        // which means that *trait selection* must be done on a copy of the population made at the beginning
        // of each step, and trait adoption/unadoption will then form the new state of the population, which
        // is Observed.
        this.getPopulation().savePreviousStepTraits();

        // Now iterate over all agents in the population and fire their rules, forming a new sample of the
        // previous step's trait distribution
        for (IAgent agent : shuffledAgentList) {
            agent.fireRules();
        }

        // Now do any analysis or observation on the results of this transmission step
        this.dimension.notifyObservers();
        //log.trace("exiting modelStep at time: " + this.currentTime);
    }


    // Used for verification and debugging ONLY
    private void checkPopulationTraits() {
        List<IAgent> agentList = this.population.getAgents();
        Map<String, Integer> traitCounts = new HashMap<String, Integer>();
        for (IAgent agent : agentList) {
            Set<ITrait> traitList = agent.getCurrentlyAdoptedTraits();
            if (traitList.size() > 1) {
                log.error("agent " + agent.getAgentID() + " has " + traitList.size() + " traits - ERROR");
            }
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
        for (String trait : sortedTraits) {
            System.out.println(trait + "\t" + traitCounts.get(trait));
        }
        log.info("number of traits found: " + traitCounts.size());
        log.info("====================================================================");
    }


    public void modelObservations() {
        log.trace("entering modelObservations at time: " + this.currentTime);
        for (ITraitStatisticsObserver<ITraitDimension> obs : this.observerList) {
            obs.perStepAction();
        }
    }

    public void modelFinalize() {
        for (ITraitStatisticsObserver<ITraitDimension> obs : this.observerList) {
            obs.endSimulationAction();
            obs.finalizeObservation();
        }
        log.info("Finalizing model run, writing historical data, and closing any files or connections");
    }

}
