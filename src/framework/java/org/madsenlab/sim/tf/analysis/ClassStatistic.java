/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.analysis;

import org.madsenlab.sim.tf.interfaces.IStatistic;
import org.madsenlab.sim.tf.interfaces.classification.IClass;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 12:34:30 PM
 */

public class ClassStatistic implements IStatistic<IClass> {
    private IClass classInstance;
    private Integer timeIndex;

    public ClassStatistic(IClass c, Integer time) {
        this.classInstance = c;
        this.timeIndex = time;
    }

    public IClass getTarget() {
        return this.classInstance;
    }

    public Integer getTimeIndex() {
        return this.timeIndex;
    }
}
