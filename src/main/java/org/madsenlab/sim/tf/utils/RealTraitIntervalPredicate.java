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

/**
 * Predicate for determining if a trait whose variation model is a real number on an interval [a,d]
 * falls into a subinterval (b,c) where a < b < c < d.
 * <p/>
 * Boolean flags in the constructor are used to indicate the open or closed status of an endpoint.  This
 * is important because (a) the default random number generator employs the open interval (0,1), and (b) we
 * should not have gaps or overlaps in adjacent intervals.  Thus, We would typically use half-closed intervals
 * to cover the entire interval (0,1) to chop up a real-valued trait space, with one final closed interval to
 * complete the series.
 * <p/>
 * For example:  (0,0.25], (0.25, 0.5], (0.5, 0.75], (0.75, 1) cleanly creates four partitions of the open
 * unit interval (0,1) with complete coverage and no overlaps.
 * <p/>
 * <p/>
 * <p/>
 * User: mark
 * Date: 6/20/12
 * Time: 1:32 PM
 */

public class RealTraitIntervalPredicate extends TraitPredicate {
    private RealInterval interval;
    private final static double EPSILON = 0.000001;

    /**
     * Constructs an interval predicate for doubles between lower and upper bound, with Boolean flags indicating
     * whether each endpoint is closed (i.e., FALSE indicates an open endpoint).
     * <p/>
     * Boundary testing is performed with a small EPSILON test 1/10^6 to deal with floating point equality issues,
     * which actually showed up randomly in repeated unit testing of the naive equality test.
     *
     * @param lowerBound
     * @param isClosedLower
     * @param upperBound
     * @param isClosedUpper
     */
    public RealTraitIntervalPredicate(double lowerBound, Boolean isClosedLower, double upperBound, Boolean isClosedUpper) {
        this.interval = new RealInterval(lowerBound, isClosedLower, upperBound, isClosedUpper);
    }

    @Override
    boolean doEvaluate(ITrait trait) {
        boolean answer = false;
        Double traitID = (Double) trait.getTraitID();

        // first, the closed endpoint cases
        if (interval.isClosedLower) {
            if (Math.abs(interval.lowerBound - traitID) < EPSILON) {
                answer = true;
            }
        }
        if (interval.isClosedUpper) {
            if (Math.abs(interval.upperBound - traitID) < EPSILON) {
                answer = true;
            }
        }


        // now, the interior
        if (traitID > interval.lowerBound && traitID < interval.upperBound) {
            answer = true;
        }

        return answer;
    }
}
