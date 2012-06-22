/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 11:52:21 AM
 */

public interface IStatisticsObserver<T> {
    /**
     * Called automatically by the publisher of the statistic being observed
     *
     * @param stat
     */
    public void updateStatistics(IStatistic<T> stat);

    /**
     * Should be called by the simulation model after every time step.  Observers may choose to make some calculation or
     * type of output on a per-time-step basis.  If an observer does so, the author should consider the operations carefully so
     * as to create minimal impact on simulation performance.  It's simply not necessary to write data to files synchronously in each
     * step, and it really slows down performance.
     * <p/>
     * On the other hand, making a simple derived calculation and stashing the result with a time index is useful and low impact,
     * and that's what this is intended for.
     */

    public void perStepAction();

    /**
     * Should be called by the simulation model after the end of the simulation run, but before the observer is "finalized."
     * This is the appropriate place to do I/O intensive operations that would slow things down, like writing the history of
     * statistics to a log file or into a database.
     */
    public void endSimulationAction();

    /**
     * finalizeObservation should be implemented by any observer which has file or network output that might need to be flushed
     * prior to ending a simulation run.
     */
    public void finalizeObservation();

}
