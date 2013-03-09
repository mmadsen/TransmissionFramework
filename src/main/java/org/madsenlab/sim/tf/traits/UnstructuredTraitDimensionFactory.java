/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.traits;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITraitFactory;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/8/13
 * Time: 1:24 PM
 */

public class UnstructuredTraitDimensionFactory {
    private ISimulationModel model;
    private Logger log;

    public UnstructuredTraitDimensionFactory(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());

    }

    public UnstructuredTraitDimension getNewTraitDimension(Class variationType, ITraitFactory traitFactory) {
        UnstructuredTraitDimension newDimension = null;
        if (variationType.getClass().equals(Double.class)) {
            newDimension = new UnstructuredTraitDimension<Double>(this.model, traitFactory);
        } else if (variationType.getClass().equals(Integer.class)) {
            newDimension = new UnstructuredTraitDimension<Integer>(this.model, traitFactory);
        }
        return newDimension;
    }
}
