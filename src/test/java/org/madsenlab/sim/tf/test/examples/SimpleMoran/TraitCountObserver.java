/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.examples.SimpleMoran;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.TraitIDComparator;

import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 3:19:51 PM
 */

//@Ignore needed to prevent JUnit from trying to execute test helper classes

@Ignore
public class TraitCountObserver implements ITraitStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;

    public TraitCountObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
    }


    public void updateTraitStatistics(ITraitStatistic<ITraitDimension> stat) {
        Integer timeIndex = stat.getTimeIndex();
        ITraitDimension dim = stat.getTarget();
        Map<ITrait, Integer> traitCountMap = dim.getCurGlobalTraitCounts();


        StringBuffer sb = new StringBuffer();
        Set<ITrait> keys = traitCountMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        for (ITrait aTrait : sortedKeys) {
            sb.append("[" + aTrait.getTraitID() + "] ");
            sb.append(traitCountMap.get(aTrait));
            sb.append(" ");
        }

        log.trace("Time: " + timeIndex + " Counts: " + sb.toString());

    }

    public void perStepAction() {

    }

    public void endSimulationAction() {

    }

    public void finalizeObservation() {

    }
}
