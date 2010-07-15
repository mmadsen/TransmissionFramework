/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.structure;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.mmadsen.sim.tf.agent.SimpleAgent;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.IAgentTag;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import sun.management.resources.agent;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 15, 2010
 * Time: 9:36:09 AM
 */

public class SimpleAgentTagProvider implements Provider<IAgentTag> {
    @Inject
    private ISimulationModel model;

    public IAgentTag get() {
        IAgentTag tag = new SimpleAgentTag();
        tag.setSimulationModel(model);
        return tag;
    }
}
