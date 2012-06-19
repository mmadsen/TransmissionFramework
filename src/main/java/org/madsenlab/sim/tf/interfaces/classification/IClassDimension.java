/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import org.madsenlab.sim.tf.interfaces.ITraitDimension;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:42 AM
 */

public interface IClassDimension {

    public void setTraitDimensionToTrack(ITraitDimension dimension);

    // TODO:  Add dimension modes
    // TODO:  Add human readable dimension description (for doing simulations on real situations)
    // TODO:  Do we need remove/revise dimension modes?  Maybe second round.

    public Integer getNumDimensionModes();


}
