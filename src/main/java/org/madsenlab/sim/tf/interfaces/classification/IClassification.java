/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces.classification;

import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/17/12
 * Time: 11:44 AM
 */

public interface IClassification {

    public Set<IClassDimension> getClassificationDimensions();

    public Integer getNumClasses();

    /**
     * Return an object capable of identifying agents or sets of traits as to class membership
     * for this classification.  Each classification has its own identifier.  This may or may not
     * be a separate Java class; if not, this method simply returns the classification object itself
     * if it implements the identifier interface.
     *
     * @return
     */
    public IClassIdentifier getClassIdentifier();


}
