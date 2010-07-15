/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.app;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.mmadsen.sim.tf.config.BasicModelModule;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 4, 2010
 * Time: 1:08:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimRunner {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModelModule());
        ISimulationModel model = injector.getInstance(ISimulationModel.class);

        /* This would be the place to parse command line parameters, etc */


        /* Set up any other static resources, connect to databases, etc etc */

        /* Start the simulation model */
        model.run();
    }

    
}
