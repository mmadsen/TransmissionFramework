/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.traits;

import org.mmadsen.sim.tf.interfaces.IAgentTag;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;

/**
 * Immutable helper object representing a combination of an ITrait and an IAgentTag
 * object.  Really only one of these objects is needed per trait/tag combo.
 * 
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 9:28:50 AM
 */

public class TraitDimensionTagCombination  {
    private ITraitDimension dim;
    private IAgentTag tag;

    public TraitDimensionTagCombination(ITraitDimension aDim, IAgentTag aTag) {
        this.dim = aDim;
        this.tag = aTag;
    }

    public ITraitDimension getTrait() { return this.dim; }

    public IAgentTag getTag() { return this.tag; }

}
