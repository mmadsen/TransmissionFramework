/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.SimpleMoran;

import org.apache.commons.cli.*;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.models.AbstractSimModel;
import org.madsenlab.sim.tf.rules.RandomCopyNeighborSingleDimensionRule;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 10:19:32 AM
 */

public class SimpleMoranDriftModel extends AbstractSimModel {
    ITraitStatisticsObserver<ITraitDimension> obs, obs2;
    ITraitDimension dimension;
    Integer numAgents;
    Double mutationRate;
    Boolean isInfiniteAlleles = false;
    Integer maxTraits;
    Integer startingTraits;

    // Undecided yet how to structure interaction rules generically so keeping them here for the moment
    List<IInteractionRule> ruleList;


    // TODO:  What to fill out in the simulation model
    // DONE - Set up a trait dimension
    // Set up an initial set of traits
    // DONE - Set up an initial set of agents
    // Randomly assign initial set of traits to initial agents
    // Set up an IPopulation for the agents, assume well-mixed
    //


    public void initializeModel() {
        // first, set up the traits in a dimension
        // second, set up agents, assigning an initial trait to each agent at random
        this.dimension = this.dimensionProvider.get();
        this.obs = new TraitCountObserver(this);
        this.obs2 = new TraitFrequencyObserver(this);

        // set up the stack of rules, to be fired in the order given in the list
        this.ruleList = new ArrayList<IInteractionRule>();
        IInteractionRule rcmRule = new RandomCopyNeighborSingleDimensionRule(this);
        this.ruleList.add(rcmRule);

        this.log.debug("Creating one dimension and " + this.startingTraits + " traits to begin");
        for (Integer i = 0; i < this.startingTraits; i++) {
            ITrait newTrait = this.traitProvider.get();
            newTrait.setTraitID(i.toString());
            newTrait.setOwningDimension(this.dimension);
            this.dimension.addTrait(newTrait);
            newTrait.attach(this.obs);
            newTrait.attach(this.obs2);
        }

        this.log.debug("Creating " + this.numAgents + " agents with random starting traits");
        for (int i = 0; i < this.numAgents; i++) {
            IAgent agent = this.getPopulation().createAgent();
            ITrait randomTrait = this.dimension.getRandomTraitFromDimension();
            randomTrait.adopt(agent);
        }

    }

    public void parseCommandLineOptions(String[] args) {
        this.log.debug("entering parseCommandLineOptions");

        Options cliOptions = new Options();
        cliOptions.addOption("n", true, "number of agents in simulation");
        cliOptions.addOption("m", true, "mutation rate in decimal form (e.g., 0.001");
        cliOptions.addOption("s", true, "starting number of traits (must not be greater than max traits value");
        cliOptions.addOption("l", true, "length of simulation in steps");

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
            this.isInfiniteAlleles = true;
            this.log.info("infinite alleles model selected - no maximum trait number");
        }
        else {
            this.isInfiniteAlleles = false;
            this.log.info("finite alleles model selected");
            this.maxTraits = Integer.parseInt(cmd.getOptionValue("f", "2"));

        }


        this.lengthSimulation = Integer.parseInt(cmd.getOptionValue("l", "10000"));
        this.numAgents = Integer.parseInt(cmd.getOptionValue("n", "1000"));
        this.mutationRate = Double.parseDouble(cmd.getOptionValue("m", "0.000001"));
        this.startingTraits = Integer.parseInt(cmd.getOptionValue("s", "2"));

        // Report starting parameters
        this.log.info("starting number of traits: " + this.startingTraits);
        this.log.info("maximum number of traits: " + this.maxTraits);
        this.log.info("number of agents: " + this.numAgents);
        this.log.info("mutation rate: " + this.mutationRate);
        this.log.info("simulation length: " + this.lengthSimulation);

        this.log.debug("exiting parseCommandLineOptions");
    }

    public void modelStep() {
        log.trace("entering modelStep at time: " + this.currentTime);

        // Testing out what kind of API might be nice for firing off rules in sequence
        // not sure what object needs to be passed to each rule, though....
        for(IInteractionRule rule: this.ruleList) {
            log.trace("executing rule: " + rule.getRuleName());
            rule.execute(null);
        }

    }

    public void modelObservations() {
        log.trace("entering modelObservations at time: " + this.currentTime);
    }


}
