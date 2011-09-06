/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.traits;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Jul 14, 2010
 * Time: 10:55:19 AM
 */

public class UnstructuredTraitProvider implements Provider<ITrait> {
    @Inject
    private ISimulationModel model;

    public ITrait get() {
        ITrait trait = new UnstructuredTrait();
        trait.setSimulationModel(model);
        return trait;
    }
}