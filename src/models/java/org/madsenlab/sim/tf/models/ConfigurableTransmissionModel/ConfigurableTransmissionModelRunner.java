/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.models.ConfigurableTransmissionModel;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/13/13
 * Time: 1:00 PM
 */

public class ConfigurableTransmissionModelRunner {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ConfigurableTransmissionModule());
        ISimulationModel model = injector.getInstance(ISimulationModel.class);
        model.parseCommandLineOptions(args);
        model.initializeRNG(false);
        model.initializeProviders();
        model.initializeModel();

        model.debugCheckInitialPopulation();

        // start the simulation model
        model.run();
    }
}
