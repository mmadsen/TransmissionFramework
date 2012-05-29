/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;


import org.madsenlab.sim.tf.interfaces.ITrait;

import java.util.Comparator;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 9:00:57 PM
 */

public class TraitIDComparator implements Comparator<ITrait> {
    /*public int compare(Object o1, Object o2) {
        Integer one = Integer.parseInt(((ITrait) o1).getTraitID());
        Integer two = Integer.parseInt(((ITrait) o2).getTraitID());
        if (one > two)
            return 1;
        else if (one < two)
            return -1;
        else
            return 0;
    }*/


    @Override
    public int compare(ITrait trait1, ITrait trait2) {
        return trait1.compareTo(trait2);
    }
}
