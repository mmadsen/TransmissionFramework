/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import org.madsenlab.sim.tf.interfaces.classification.IClassDimensionMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/28/13
 * Time: 9:49 AM
 */

public class ModeSetUniqueIdentifier {

    public static Integer getUniqueIdentifier(Set<IClassDimensionMode> modeset) {

        List<IClassDimensionMode> sortedModeset = new ArrayList<IClassDimensionMode>(modeset);
        Collections.sort(sortedModeset);

        // now we can do the hashcode of the modeset, because we'll have always sorted the same modes into the same order
        return sortedModeset.hashCode();

    }


}
