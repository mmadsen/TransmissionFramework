/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;

import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 12:53:50 PM
 */

public interface IStatisticsSubject {

    public void attach(ITraitStatisticsObserver obs);

    public void attach(List<ITraitStatisticsObserver<ITraitDimension>> obsList);

    public void detach(ITraitStatisticsObserver obs);

    public void detach(List<ITraitStatisticsObserver<ITraitDimension>> obsList);

    public Integer getNumObservers();

    public void notifyObservers();

}
