/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.structure;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.madsenlab.sim.tf.interfaces.IInteractionTopology;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;


/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 12/11/10
 * Time: 3:28 PM
 */


public class WellMixedWithinDemeTopologyProvider implements Provider<IInteractionTopology> {
    @Inject
    public ISimulationModel model;


    public IInteractionTopology get() {
        WellMixedWithinDemeTopology topology = new WellMixedWithinDemeTopology();
        topology.initialize(model);
        return topology;
    }
}
