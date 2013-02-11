/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.agent;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.madsenlab.sim.tf.interfaces.IAgent;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 14, 2010
 * Time: 9:12:38 AM
 */

public class UnstructuredMultidimensionalTraitAgentProvider implements Provider<IAgent> {
    @Inject
    private ISimulationModel model;

    public IAgent get() {
        IAgent agent = new UnstructuredMultidimensionalTraitAgent();
        agent.setSimulationModel(model);
        return agent;
    }
}
