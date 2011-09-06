/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.app;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.madsenlab.sim.tf.config.BasicModelModule;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 4, 2010
 * Time: 1:08:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimRunner {
    @Inject
    public ISimulationModel model;

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModelModule());
        ISimulationModel model = injector.getInstance(ISimulationModel.class);

        // Parse the command line options, in case they select population options
        model.parseCommandLineOptions(args);


        // Initialize a starting population
        model.initializeProviders();



        /* Set up any other static resources, connect to databases, etc etc */

        /* Start the simulation model */
        model.run();
    }


}
