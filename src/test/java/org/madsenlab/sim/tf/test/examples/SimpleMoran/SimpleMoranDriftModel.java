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
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.ITraitDimension;
import org.madsenlab.sim.tf.interfaces.ITraitStatisticsObserver;
import org.madsenlab.sim.tf.models.AbstractSimModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 10:19:32 AM
 */

public class SimpleMoranDriftModel extends AbstractSimModel {
    ITraitStatisticsObserver<ITraitDimension> obs;
    ITraitDimension dimension;
    Integer numAgents;
    Double mutationRate;
    Boolean isInfiniteAlleles = false;
    Integer maxTraits;
    Integer startingTraits;


    // TODO:  What to fill out in the simulation model
    // DONE - Set up a trait dimension
    // Set up an initial set of traits
    // DONE - Set up an initial set of agents
    // Randomly assign initial set of traits to initial agents
    // Set up an IPopulation for the agents, assume well-mixed
    //


    private void setup() {
        this.obs = new TraitCountObserver(this);
        this.dimension = this.getTraitDimensionProvider().get();



    }

    public void run() {

    }


    public void initializePopulation() {
        // first, set up the traits in a dimension
        // second, set up agents, assigning an initial trait to each agent at random


        for (int i = 0; i < this.numAgents; i++) {
            IAgent agent = this.getPopulation().createAgent();
        }

    }

    public void parseCommandLineOptions(String[] args) {
        this.log.debug("entering parseCommandLineOptions");

        Options cliOptions = new Options();
        cliOptions.addOption("n", true, "number of agents in simulation");
        cliOptions.addOption("m", true, "mutation rate in decimal form (e.g., 0.001");
        cliOptions.addOption("s", true, "starting number of traits (must not be greater than max traits value");

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

        this.numAgents = Integer.parseInt(cmd.getOptionValue("n", "1000"));
        this.mutationRate = Double.parseDouble(cmd.getOptionValue("m", "0.000001"));
        this.startingTraits = Integer.parseInt(cmd.getOptionValue("s", "2"));

        // Report starting parameters
        this.log.info("starting number of traits: " + this.startingTraits);
        this.log.info("maximum number of traits: " + this.maxTraits);
        this.log.info("number of agents: " + this.numAgents);
        this.log.info("mutation rate: " + this.mutationRate);

        this.log.debug("exiting parseCommandLineOptions");
    }
}
