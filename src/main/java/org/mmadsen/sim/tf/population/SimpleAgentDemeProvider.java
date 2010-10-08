/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.population;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IDeme;
import org.mmadsen.sim.tf.interfaces.IPopulation;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 30, 2010
 * Time: 2:22:27 PM
 */

public class SimpleAgentDemeProvider implements Provider<IDeme> {

    @Inject
    public ISimulationModel model;


    public IDeme get() {
        SimpleAgentDeme deme = new SimpleAgentDeme();
        deme.initialize(model, this);
        return deme;
    }

}
