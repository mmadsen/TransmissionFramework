/*
 * Copyright (c) 2012.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.analysis;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.TraitIDComparator;

import java.io.PrintWriter;
import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 3:19:51 PM
 */


public class GlobalTraitCountObserver implements ITraitStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private Map<ITrait, Integer> traitCountMap;
    private Integer lastTimeIndexUpdated;
    private PrintWriter pw;
    private Map<Integer,Map<ITrait,Integer>> histTraitCount;

    public GlobalTraitCountObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.histTraitCount = new HashMap<Integer,Map<ITrait, Integer>>();

        String traitFreqLogFile = this.model.getModelConfiguration().getProperty("global-trait-count-logfile");
        log.debug("traitCountLogFile: " + traitFreqLogFile);
        this.pw = this.model.getLogFileHandler().getFileWriterForPerRunOutput(traitFreqLogFile);
//        try{
//            this.pw = new PrintWriter(new BufferedWriter(new FileWriter("trait-frequencies-by-time.txt")));
//        } catch(IOException ex) {
//            log.error("ERROR CREATING TRAIT FREQUENCIES BY TIME LOG FILE");
//            System.exit(1);
//        }
    }

    public void updateTraitStatistics(ITraitStatistic<ITraitDimension> stat) {
        log.trace("entering updateTraitStatistics");
        this.lastTimeIndexUpdated = stat.getTimeIndex();
        ITraitDimension dim = stat.getTarget();
        Map<ITrait,Integer> countMap = dim.getCurGlobalTraitCounts();
        this.histTraitCount.put(this.lastTimeIndexUpdated,countMap);
    }

    public void perStepAction() {
        log.trace("entering perStepAction");

        //log.trace("histTraitFreq: " + this.histTraitFreq);
        this.printFrequencies();

        Integer tick = this.model.getCurrentModelTime();
        // Every 100 ticks, write historical data to disk and flush it to keep memory usage and performance reasonable.
        if(tick % 100 == 0) {
            this.logFrequencies();
            this.histTraitCount.clear();
        }
    }

    public void endSimulationAction() {
        log.trace("entering endSimulationAction");
        this.logFrequencies();
    }

    public void finalizeObservation() {
        log.trace("entering finalizeObservation");
            this.pw.flush();
            this.pw.close();
    }

    private void printFrequencies() {
        Integer time = this.model.getCurrentModelTime();

        Map<ITrait,Integer> countMap = this.histTraitCount.get(time);
        //log.debug("printCounts - getting counts for time: " + time + " : " + countMap);

        StringBuffer sb = prepareCountLogString(time, countMap);
        log.debug(sb.toString());
        sb = null;

    }

    private StringBuffer prepareCountLogString(Integer time, Map<ITrait,Integer> countMap) {
        //log.debug("prepare string: freqMap: " + freqMap);
        Integer numNonZeroTraits = 0;
        Integer totalOfTraitCounts = 0;
        StringBuffer sb = new StringBuffer();
        Set<ITrait> keys = countMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        sb.append(time);
        sb.append(",");
        for (ITrait aTrait : sortedKeys) {
            Integer count = countMap.get(aTrait);
            sb.append(aTrait.getTraitID());
            sb.append(":");
            sb.append(count);
            sb.append(",");
            if(count != 0) {
                numNonZeroTraits++;
            }
            totalOfTraitCounts += count;
        }
        sb.append("tot:");
        sb.append(totalOfTraitCounts);
        sb.append(",numtrait:");
        sb.append(numNonZeroTraits);
        return sb;
    }

    
    
    private void logFrequencies() {
        log.trace("entering logFrequencies");
        Set<Integer> keys = this.histTraitCount.keySet();
        List<Integer> sortedKeys = new ArrayList<Integer>(keys);
        Collections.sort(sortedKeys);
        for(Integer time: sortedKeys) {
            Map<ITrait,Integer> countMap = this.histTraitCount.get(time);
            StringBuffer sb = prepareCountLogString(time, countMap);
            sb.append('\n');
            this.pw.write(sb.toString());
            //this.pw.flush();
        }

    }
}
