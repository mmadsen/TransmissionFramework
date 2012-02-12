/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.traits;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.exceptions.TraitDurationIncompleteException;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 4, 2010
 * Time: 10:59:12 AM
 */

public abstract class AbstractObservableTrait implements ITrait {
    protected Integer traitLifetime = 0;
    protected Integer tickTraitIntroduced;
    protected Integer tickTraitExited;

    protected List<ITraitStatisticsObserver> observers;
    protected ISimulationModel model;
    protected Logger log;

    public void attach(ITraitStatisticsObserver obs) {
        synchronized (this.observers) {
            log.trace("attaching to obs: " + obs);
            this.observers.add(obs);
        }
    }

    public void attach(List<ITraitStatisticsObserver<ITraitDimension>> obsList) {
        for (ITraitStatisticsObserver obs : obsList) {
            this.attach(obs);
        }
    }

    public void detach(ITraitStatisticsObserver obs) {
        synchronized (this.observers) {
            this.observers.remove(obs);
        }

    }

    public void detach(List<ITraitStatisticsObserver<ITraitDimension>> obsList) {
        for (ITraitStatisticsObserver obs : obsList) {
            this.detach(obs);
        }
    }

    public Integer getNumObservers() {
        return this.observers.size();
    }

    public void notifyObservers() {
        ITraitStatistic stat = this.getChangeStatistic();
        log.debug("change statistic: " + stat);
        for (ITraitStatisticsObserver obs : this.observers) {
            log.debug("notify observer: " + obs);
            obs.updateTraitStatistics(stat);
        }
    }

    public abstract ITraitStatistic getChangeStatistic();


    @Override
    public Boolean hasCompleteDuration() {
        if (this.traitLifetime > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getTraitDuration() {
        if (this.traitLifetime == 0) {
            throw new TraitDurationIncompleteException(this.getClass(), "getTraitDuration");
        }
        return this.traitLifetime;
    }
}
