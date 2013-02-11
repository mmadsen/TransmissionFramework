/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 10:55 AM
 */

public class RealInterval {
    public final Double lowerBound;
    public final Double upperBound;
    public final Boolean isClosedLower;
    public final Boolean isClosedUpper;

    public RealInterval(double lowerBound, Boolean isClosedLower, double upperBound, Boolean isClosedUpper) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.isClosedLower = isClosedLower;
        this.isClosedUpper = isClosedUpper;
    }


}
