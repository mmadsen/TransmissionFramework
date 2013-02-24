/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.observers;

import org.madsenlab.sim.tf.interfaces.IStatistic;
import org.madsenlab.sim.tf.interfaces.classification.IClassification;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/22/12
 * Time: 3:56 PM
 */

public class ClassificationStatistic implements IStatistic<IClassification> {
    private IClassification classification;
    private Integer timeIndex;

    public ClassificationStatistic(IClassification cf, Integer time) {
        this.classification = cf;
        this.timeIndex = time;
    }

    public IClassification getTarget() {
        return this.classification;
    }

    public Integer getTimeIndex() {
        return this.timeIndex;
    }
}
