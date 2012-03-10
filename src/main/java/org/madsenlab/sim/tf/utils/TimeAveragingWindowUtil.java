/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
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

public class TimeAveragingWindowUtil {
    private ISimulationModel model;
    private Logger log;
    
    public TimeAveragingWindowUtil(ISimulationModel model) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());
    }

    public List<Integer> calculateTimeAvWindows() {
        GenerationDynamicsMode timeRate = this.model.getModelConfiguration().getModelRateTimeRuns();
        Integer numTicksPerGeneration;

        // First, we want to normalize what a "generation" is, since Moran models
        // need N ticks to equal the same number of copying events as 1 tick in WF
        if (timeRate == GenerationDynamicsMode.CONTINUOUS) {
            numTicksPerGeneration = this.model.getModelConfiguration().getNumAgents();
        } else {
            numTicksPerGeneration = 1;
        }

        int generationsInRun = this.model.getModelConfiguration().getLengthSimulation() / numTicksPerGeneration;
        log.trace("generations in run: " + generationsInRun);

        // For each simulation run, we want at least 10 window samples, at the *longest* window size
        // For example, if we have 2000 generations in WF, we should only time-average up to 100 generations,
        // and this would give us 20 time-averaged blocks of 100 generations each.
        int numSamples = (int) (generationsInRun * 0.1);
        log.trace("2% of generations for max window size: " + numSamples);


        // Future ref, if we want intervals in powers of 2:  Math.Ceil(Math.log(x)/Math.log(2))

        // Now figure out 
        List<Integer> windowSizes = new ArrayList<Integer>();

        int nextWindow = numSamples;
        windowSizes.add(nextWindow);
        log.trace("nextWindow = " + nextWindow);
        // The minimum window size we want is 3 generations
        while (nextWindow > 3) {
            int n = (int) nextWindow / 2;
            log.trace("nextWindow = " + n);
            windowSizes.add(n);
            nextWindow = n;
        }

        // now convert generations back into tick sizes for a given model
        return windowSizes;
    }
    
    public List<Integer> getLongTimeAvWindows() {
        if(this.model.getModelConfiguration().getLengthSimulation() < 19000) {
            log.error("Cannot use getLongTimeAvWindows on short simulation runs - try length > 20000");
            System.exit(1);
        }
        List<Integer> windowList = new ArrayList<Integer>();
        int length = this.model.getModelConfiguration().getLengthSimulation();
        windowList.add( (int)( length * 0.20 ));
        windowList.add( (int)(length * 0.10));
        windowList.add( (int)(length * 0.05));
        windowList.add( (int)(length * 0.025));
        windowList.add( (int)(length * 0.0125));
        windowList.add( (int)(length * 0.00625));
        return windowList;
    }
}