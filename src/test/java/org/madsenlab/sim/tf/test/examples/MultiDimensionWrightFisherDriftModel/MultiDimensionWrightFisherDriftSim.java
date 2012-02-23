/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.MultiDimensionWrightFisherDriftModel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.madsenlab.sim.tf.app.SimRunner;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.test.examples.WrightFisherDriftModel.WrightFisherDriftModule;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Nov 26, 2010
 * Time: 9:55:24 AM
 */



public class MultiDimensionWrightFisherDriftSim extends SimRunner {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MultiDimensionWrightFisherDriftModule());
        ISimulationModel model = injector.getInstance(ISimulationModel.class);

        // Parse the command line options and initialize logging
        model.parseCommandLineOptions(args);
        model.initializeConfigurationAndLoggingFromProperties();


        // Initialize RNG -- true makes the stream replicable, false uses timestamp as seed
        model.initializeRNG(false);
        model.initializeProviders();

        /* Given the choices made on the command line, initialize the starting population */
        model.initializeModel();

        /* Start the simulation model */
        model.run();


    }



}
