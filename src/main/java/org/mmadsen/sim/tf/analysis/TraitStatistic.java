/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.analysis;

import org.mmadsen.sim.tf.interfaces.ITraitDimension;
import org.mmadsen.sim.tf.interfaces.ITraitStatistic;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 12:34:30 PM
 */

public class TraitStatistic implements ITraitStatistic<ITraitDimension> {
    private ITraitDimension dim;
    private Integer timeIndex;

    public TraitStatistic(ITraitDimension aDimension, Integer time) {
        this.dim = aDimension;
        this.timeIndex = time;
    }

    public ITraitDimension getTarget() { return this.dim; }

    public Integer getTimeIndex() { return this.timeIndex;}
}
