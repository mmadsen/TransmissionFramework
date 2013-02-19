/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.  
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.utils;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/17/12
 * Time: 1:04 PM
 */

public class Log2HistogramBinUtil {
    private ISimulationModel model;
    private Logger log;

    public Log2HistogramBinUtil(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());

    }

    public double[] getLog2HistogramBins() {
        int popsize = this.model.getModelConfiguration().getNumAgents();
        int len = this.model.getModelConfiguration().getLengthSimulation();
        // If one trait completely predominates, say in a conformist model, we want
        // to be able to bin it.  
        int maxCount = popsize * len;
        log.trace("Avg highest count possible for simulation: " + maxCount);
        int binEdge = 1;
        List<Integer> binEdgeList = new ArrayList<Integer>();
        binEdgeList.add(binEdge);
        while (binEdge < maxCount) {
            binEdge = binEdge * 2;
            binEdgeList.add(binEdge);
        }
        double[] edgeArray = new double[binEdgeList.size()];
        for (int i = 0; i < binEdgeList.size(); i++) {
            edgeArray[i] = binEdgeList.get(i);
        }
        log.trace("bin edges: " + binEdgeList);
        return edgeArray;
    }
}
