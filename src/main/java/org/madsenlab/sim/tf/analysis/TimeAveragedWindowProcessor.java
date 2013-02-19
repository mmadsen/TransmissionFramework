/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.  
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.analysis;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.ITrait;
import org.madsenlab.sim.tf.utils.TraitIDComparator;

import java.io.PrintWriter;
import java.util.*;

/**
 * The task of a TimeAveragedWindowProcessor is to handle sampling at a specific window size (e.g., 50 generations),
 * when handed a trait count update.  It does this by creating a sequence of TraitCountsOverInterval objects with
 * that specific windowsize, passing each TCOI object in sequence the incoming trait counts.  The sequence of TCOI
 * objects then represents the sequence of trait counts for the simulation run, aggregated into blocks of windowsize.
 * <p/>
 * There may be, and usually will be, more than one TimeAveragedWindowProcessor receiving the same stream of trait
 * counts, at different window sizes.  To prevent memory explosion, when each TCOI object is "full" (i.e., has received
 * windowsize samples), this class will write the accumulated counts to the per-windowsize log file, along with start
 * and end time indices.  That TCOI object is then destroyed and a new one created.
 * <p/>
 * <p/>
 * User: mark
 * Date: 2/15/12
 * Time: 12:58 PM
 */

public class TimeAveragedWindowProcessor {
    private ISimulationModel model;
    private Logger log;
    private Integer windowSize;
    private PrintWriter wsPW;
    private PrintWriter ewensPW;
    private PrintWriter knPW;
    private TraitCountsOverInterval currentAggregateSample;
    private List<Integer> totalTraitsPerWindow;
    private Map<Integer, Map<ITrait, Integer>> histSamples;
    private List<Integer> numTraitsInSamplePerTick;


    public TimeAveragedWindowProcessor(ISimulationModel model, Integer windowSize, String logBaseName, String ewensBaseName, String knSampleBase) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());
        this.windowSize = windowSize;
        this.histSamples = new HashMap<Integer, Map<ITrait, Integer>>();
        this.numTraitsInSamplePerTick = new ArrayList<Integer>();

        // Initialize log file for this window size
        StringBuffer sb = new StringBuffer();
        sb.append(logBaseName);
        sb.append(windowSize);
        this.wsPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(sb.toString());

        // Initialize a Ewens sample log for this window size
        StringBuffer sb2 = new StringBuffer();
        sb2.append(ewensBaseName);
        sb2.append(windowSize);
        this.ewensPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(sb2.toString());

        // Initialize log file for kn samples per window
        StringBuffer sb3 = new StringBuffer();
        sb3.append(knSampleBase);
        sb3.append(windowSize);
        this.knPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(sb3.toString());

        // initial sample for the first window
        this.currentAggregateSample = new TraitCountsOverInterval(this.model, this.windowSize);
        this.currentAggregateSample.startCounting(this.model.getCurrentModelTime());

        this.totalTraitsPerWindow = new ArrayList<Integer>();
    }

    public void updateTraitStatistics(Map<ITrait, Integer> countMap) {
        if (this.currentAggregateSample.hasCapacity()) {
            // add the current count map to the current sample
            this.currentAggregateSample.putTraitCountsForTick(countMap);
        } else {
            // No more room in the current sample, so log it, nuke it, and start a new one...
            this.currentAggregateSample.endCounting(this.model.getCurrentModelTime());
            // first log the previous sample
            StringBuffer sb = this.prepareCountLogString(this.currentAggregateSample);
            this.wsPW.write(sb.toString());
            this.wsPW.write("\n");

            // record the total number of traits for stats
            this.totalTraitsPerWindow.add(this.currentAggregateSample.getIntervalCountMap().keySet().size());


            // take a Ewens sample from this window before we lose the detailed data.
            Map<ITrait, Integer> sampleMap = this.takeEwensSampleFromCurrentWindow();
            // record the number of traits seen in the sample (k_n)
            this.numTraitsInSamplePerTick.add(sampleMap.keySet().size());
            // record the sample itself
            StringBuffer sb2 = this.prepareSlatkinTestInputString(this.model.getCurrentModelTime(), sampleMap);
            this.ewensPW.write(sb2.toString());
            this.ewensPW.write("\n");
            this.ewensPW.flush();

            // destroy the previous sample
            this.currentAggregateSample = null;

            // create a new sample
            this.currentAggregateSample = new TraitCountsOverInterval(this.model, this.windowSize);
            this.currentAggregateSample.startCounting(this.model.getCurrentModelTime());
            // add the current countMap to the new sample
            this.currentAggregateSample.putTraitCountsForTick(countMap);
        }


    }

    public DescriptiveStatistics calculateStatisticsAndFinalizeLogs() {
        // we're done and write their final logs, even if we have a partial sample, which could happen
        // for the end of the simulation given integer rounding in determining the window sizes.
        StringBuffer sb = this.prepareCountLogString(this.currentAggregateSample);
        this.wsPW.write(sb.toString());
        this.wsPW.write("\n");

        // Write the k_n counts for each windowsize sample to its log
        this.logKnSamples();

        // Now calculate stats
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (Integer val : this.totalTraitsPerWindow) {
            stats.addValue((double) val);
        }
        return stats;
    }

    public void finalizeObservation() {
        this.wsPW.flush();
        this.wsPW.close();
        this.ewensPW.flush();
        this.ewensPW.close();
        this.knPW.flush();
        this.knPW.close();
    }


    private StringBuffer prepareCountLogString(TraitCountsOverInterval traitCountsOverInterval) {
        Integer numNonZeroTraits = 0;
        Integer totalOfTraitCounts = 0;
        StringBuffer sb = new StringBuffer();
        Map<ITrait, Integer> countMap = traitCountsOverInterval.getIntervalCountMap();
        Set<ITrait> keys = countMap.keySet();
        List<ITrait> sortedKeys = new ArrayList<ITrait>(keys);
        Collections.sort(sortedKeys, new TraitIDComparator());
        sb.append(traitCountsOverInterval.getIntervalStart());
        sb.append(",");
        sb.append(traitCountsOverInterval.getIntervalEnd());
        sb.append(",");
        for (ITrait aTrait : sortedKeys) {
            Integer count = countMap.get(aTrait);
            sb.append(aTrait.getTraitID());
            sb.append(":");
            sb.append(count);
            sb.append(",");
            if (count != 0) {
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

    private StringBuffer prepareSlatkinTestInputString(Integer time, Map<ITrait, Integer> sampleMap) {
        StringBuffer sb = new StringBuffer();
        // I really think it's stupid that values() returns something generic so I have to allocate a whole
        // separate object just to sort the damned thing
        Collection<Integer> countSet = sampleMap.values();
        List<Integer> countList = new ArrayList<Integer>(countSet);
        Collections.sort(countList);   // this puts it in ascending order
        Collections.reverse(countList); // this puts it in the descending order req'd by slatkin-enumerate.c
        sb.append(time);
        sb.append("\t");
        for (Integer i : countList) {
            sb.append(i);
            sb.append(" ");
        }
        return sb;
    }

    public Integer getWindowSize() {
        return windowSize;
    }

    private Map<ITrait, Integer> takeEwensSampleFromCurrentWindow() {
        Map<ITrait, Integer> sampleMap = new HashMap<ITrait, Integer>();
        Integer ewensSampleSize = this.model.getModelConfiguration().getEwensSampleSize();

        // We don't actually have agents to sample from all of the generations in the TA sample, but
        // it should be equivalent to take a random sample of size N of traits held in the current window,
        // with replacement, with each trait being represented by its count (thus increasing the chance of selecting
        // a trait with higher count.
        List<ITrait> expandedTraitList = new ArrayList<ITrait>();
        Map<ITrait, Integer> countMap = this.currentAggregateSample.getIntervalCountMap();
        for (ITrait trait : countMap.keySet()) {
            int numCopies = countMap.get(trait);
            for (int i = 0; i < numCopies; i++) {
                expandedTraitList.add(trait);
            }
        }

        int samplesTaken = 0;
        while (samplesTaken < ewensSampleSize) {
            int rnd = this.model.getUniformRandomInteger(expandedTraitList.size() - 1);
            ITrait sampledTrait = expandedTraitList.get(rnd);
            if (sampleMap.containsKey(sampledTrait)) {
                int cnt = sampleMap.get(sampledTrait);
                cnt++;
                sampleMap.put(sampledTrait, cnt);
            } else {
                sampleMap.put(sampledTrait, 1);
            }
            samplesTaken++;
        }

        return sampleMap;
    }

    private void logKnSamples() {
        for (Integer count : this.numTraitsInSamplePerTick) {
            this.knPW.write(count.toString());
            this.knPW.write("\n");
        }
    }
}
