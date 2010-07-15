/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.test;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 10, 2010
 * Time: 3:40:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgentFixtureProvider implements Provider<IAgent> {
    @Inject
    private ISimulationModel model;

    public IAgent get() {
        IAgent agent = new AgentFixture();
        agent.setSimulationModel(model);
        return agent;
    }
}
