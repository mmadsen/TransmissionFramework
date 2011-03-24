/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
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
public class TraitFrequencyObserver implements ITraitStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private Map<ITrait, Double> traitFreqMap;
    private Integer lastTimeIndexUpdated;

    public TraitFrequencyObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        // initialize the map only to keep a NPE from happening if accessed before an publisher updates us.
        this.traitFreqMap = new HashMap<ITrait,Double>();
    }


    public void updateTraitStatistics(ITraitStatistic<ITraitDimension> stat) {
        this.lastTimeIndexUpdated = stat.getTimeIndex();
        ITraitDimension dim = stat.getTarget();
        this.traitFreqMap = dim.getCurGlobalTraitFrequencies();

    }

    public void printFrequencies() {


        StringBuffer sb = new StringBuffer();
        Set<ITrait> keys = this.traitFreqMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        for (ITrait aTrait : sortedKeys) {
            sb.append("[" + aTrait.getTraitID() + "] ");
            sb.append(traitFreqMap.get(aTrait));
            sb.append(" ");
        }

        log.trace("Time: " + this.lastTimeIndexUpdated + " Freq: " + sb.toString());

    }
}
