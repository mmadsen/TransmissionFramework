/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.SimpleMoran;

import org.apache.commons.cli.*;
import org.madsenlab.sim.tf.analysis.GlobalTraitCountObserver;
import org.madsenlab.sim.tf.analysis.GlobalTraitFrequencyObserver;
import org.madsenlab.sim.tf.config.GlobalModelConfiguration;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.models.AbstractSimModel;
import org.madsenlab.sim.tf.rules.CopyOrMutateDecisionRule;
import org.madsenlab.sim.tf.rules.FiniteKAllelesMutationRule;
import org.madsenlab.sim.tf.rules.InfiniteAllelesMutationRule;
import org.madsenlab.sim.tf.rules.RandomCopyNeighborSingleDimensionRule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 10:19:32 AM
 */

public class SimpleMoranDriftModel extends AbstractSimModel {
    GlobalTraitCountObserver countObserver;
    GlobalTraitFrequencyObserver freqObserver;
    ITraitDimension dimension;
    Integer numAgents;
    Double mutationRate;
    Boolean isInfiniteAlleles = false;
    Integer maxTraits;
    Integer startingTraits;

    List<IActionRule> ruleList;



    public SimpleMoranDriftModel() {
        this.modelNamePrefix = "SimpleMoranDrift";
    }

    public void initializeModel() {
        // first, set up the traits in a dimension
        // second, set up agents, assigning an initial trait to each agent at random
        this.dimension = this.dimensionProvider.get();
        this.dimensionList.add(this.dimension);


        // Now can initialize Observers
        this.countObserver = new GlobalTraitCountObserver(this);
        this.freqObserver = new GlobalTraitFrequencyObserver(this);
        this.observerList.add(this.countObserver);
        this.observerList.add(this.freqObserver);
        this.dimension.attach(this.observerList);

        // set up the stack of rules, to be fired in the order given in the list
        // in this first simulation, all agents get the same rule, but this need not be the
        // case - plan for heterogeneity!!
        this.ruleList = new ArrayList<IActionRule>();
        IInteractionRule decisionRule = new CopyOrMutateDecisionRule(this);
        IInteractionRule rcmRule = new RandomCopyNeighborSingleDimensionRule(this);
        decisionRule.registerSubRule(rcmRule);


        if(this.isInfiniteAlleles) {
            IInteractionRule iiMutation = new InfiniteAllelesMutationRule(this);
            decisionRule.registerSubRule(iiMutation);
        }
        else {
            IInteractionRule fkMutation = new FiniteKAllelesMutationRule(this);
            decisionRule.registerSubRule(fkMutation);
        }

        // Just add the decision rule, since it has two subrules
        this.ruleList.add(decisionRule);


        this.log.debug("Creating one dimension and " + this.params.getStartingTraits() + " traits to begin");
        for (Integer i = 0; i < this.params.getStartingTraits(); i++) {
            ITrait newTrait = this.traitProvider.get();
            newTrait.setTraitID(i.toString());
            newTrait.setOwningDimension(this.dimension);
            this.dimension.addTrait(newTrait);
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
        Map<ITrait, Double> freqMap = this.dimension.getCurGlobalTraitFrequencies();
        for(Map.Entry<ITrait,Double> entry: freqMap.entrySet()) {
            log.trace("Trait " + entry.getKey().getTraitID() + " Freq: " + entry.getValue().toString());
        }


    }

    public void parseCommandLineOptions(String[] args) {
        //this.log.debug("entering parseCommandLineOptions");

        this.params = new GlobalModelConfiguration(this);

        Options cliOptions = new Options();
        cliOptions.addOption("n", true, "number of agents in simulation");
        cliOptions.addOption("m", true, "mutation rate in decimal form (e.g., 0.001");
        cliOptions.addOption("s", true, "starting number of traits (must not be greater than max traits value");
        cliOptions.addOption("l", true, "length of simulation in steps");
        cliOptions.addOption("p", true, "pathname of properties file giving general model configuration (e.g., log file locations)");

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
            cmd = parser.parse( cliOptions, args );
        }
        catch( ParseException ex ) {
            System.out.println("ERROR: Command line exception: " + ex.toString());
            System.exit(1);
        }

        if(cmd.hasOption("i")) {
            this.params.setInfiniteAlleles(true);
            this.isInfiniteAlleles = true;
            //this.log.info("infinite alleles model selected - no maximum trait number");
            this.params.setMaxTraits(Integer.MAX_VALUE);
        }
        else {
            this.params.setInfiniteAlleles(false);
            this.isInfiniteAlleles = false;
            //this.log.info("finite alleles model selected");
            this.params.setMaxTraits(Integer.parseInt(cmd.getOptionValue("f", "2")));
        }


        this.params.setLengthSimulation(Integer.parseInt(cmd.getOptionValue("l", "10000")));
        // this is the only parameter that's directly needed in a superclass implementation
        this.lengthSimulation = this.params.getLengthSimulation();
        this.params.setNumAgents(Integer.parseInt(cmd.getOptionValue("n", "1000")));
        this.params.setMutationRate(Double.parseDouble(cmd.getOptionValue("m", "0.000001")));
        this.params.setStartingTraits(Integer.parseInt(cmd.getOptionValue("s", "2")));
        this.propertiesFileName = cmd.getOptionValue("p", "tf-configuration.properties");
        System.out.println("properties File: " + this.propertiesFileName);

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
        log.trace("entering modelStep at time: " + this.currentTime);

        // pick a random agent, and fire its rules stack....
        IAgent focalAgent = this.getPopulation().getAgentAtRandom();
        log.trace("agent " + focalAgent.getAgentID() + " - firing rules");
        focalAgent.fireRules();
        this.dimension.notifyObservers();
    }

    public void modelObservations() {
        log.trace("entering modelObservations at time: " + this.currentTime);
        for(ITraitStatisticsObserver<ITraitDimension> obs: this.observerList) {
           obs.perStepAction();
        }
    }

    public void modelFinalize() {
       for(ITraitStatisticsObserver<ITraitDimension> obs: this.observerList) {
           obs.endSimulationAction();
           obs.finalizeObservation();
       }
       log.info("Finalizing model run, writing historical data, and closing any files or connections");
    }

}
