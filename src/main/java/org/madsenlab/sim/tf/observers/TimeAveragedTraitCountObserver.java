/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.observers;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.*;
import org.madsenlab.sim.tf.utils.TimeAveragingWindowUtil;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Like its sibling (GlobalTraitCountObserver), the TimeAveragedTraitCountObserver receives a stream of per-tick
 * trait counts from the ITraitDimension being tracked.  An efficient strategy is needed, so we construct a
 * list of objects each of which manages a single size of time-averaging window, aggregating the incoming counts for
 * that window size, and maintaining its own log.  So no actual logging is done within this object, which serves
 * to construct and contain the per-windowsize processors, and pass them incoming data.
 * <p/>
 * This strategy, of course, can be used for tracking time-averaging whose window size varies during a run, but
 * after equilibrium, the statistics of that would be derivable from comparing different window sizes.  The only
 * reason to build variable duration processors would be to see what the effect of our ability to seriate or fit
 * exact frequency change patterns is, after variable duration averaging.  (also, it might be useful to time average
 * different demes differently....)
 * <p/>
 * <p/>
 * User: mark
 * Date: 2/14/12
 * Time: 10:09 AM
 */

public class TimeAveragedTraitCountObserver implements IStatisticsObserver<ITraitDimension> {
    private ISimulationModel model;
    private Logger log;
    private PrintWriter pw;
    private List<Integer> samplingIntervals;
    private Map<Integer, Map<ITrait, Integer>> histTraitCount;
    private String perWindowSizeBaseLogFile;
    private List<TimeAveragedWindowProcessor> windowProcessorList;
    private String perWindowSizeBaseStatFile;
    private String perWindowSizeEwensBaseFile;
    private String perWindowsSizeKnSampleBaseFile;
    private PrintWriter statPW;
    private TimeAveragingWindowUtil timeAveragingWindowUtil;

    public TimeAveragedTraitCountObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        int length = this.model.getModelConfiguration().getLengthSimulation();
        this.timeAveragingWindowUtil = new TimeAveragingWindowUtil(this.model);
        this.histTraitCount = new HashMap<Integer, Map<ITrait, Integer>>();
        this.perWindowSizeBaseLogFile = this.model.getModelConfiguration().getProperty("ta-per-windowsize-count-log-base");
        this.perWindowSizeBaseStatFile = this.model.getModelConfiguration().getProperty("ta-per-windowsize-count-stats");
        this.perWindowSizeEwensBaseFile = this.model.getModelConfiguration().getProperty("ta-per-windowsize-ewens-sample-base");
        this.perWindowsSizeKnSampleBaseFile = this.model.getModelConfiguration().getProperty("ta-per-windowsize-kn-sample-base");
        this.statPW = this.model.getLogFileHandler().getFileWriterForPerRunOutput(this.perWindowSizeBaseStatFile);
        this.windowProcessorList = new ArrayList<TimeAveragedWindowProcessor>();

        // Figure out the sampling intervals for time-averaging, and create a window processor for each
        this.samplingIntervals = this.calculateTimeAvWindows();
        for (Integer interval : this.samplingIntervals) {
            this.windowProcessorList.add(new TimeAveragedWindowProcessor(this.model, interval, this.perWindowSizeBaseLogFile, this.perWindowSizeEwensBaseFile, this.perWindowsSizeKnSampleBaseFile));
        }
    }

    @Override
    public void updateStatistics(IStatistic<ITraitDimension> stat) {
        log.trace("entering updateStatistics");
        Integer lastTimeIndexUpdated;
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getTimeStartStatistics()) {
            lastTimeIndexUpdated = stat.getTimeIndex();
            ITraitDimension dim = stat.getTarget();
            Map<ITrait, Integer> countMap = dim.getCurGlobalTraitCounts();

            // now, pass the countMap to all of the Processors...
            for (TimeAveragedWindowProcessor proc : this.windowProcessorList) {
                log.trace("updating processor " + proc.getWindowSize());
                proc.updateTraitStatistics(countMap);
            }
        }
    }

    @Override
    public void perStepAction() {
        // no per step action, at any given tick, not all of the processors would be "ready" to log their counts,
        // and partial counts are meaningless.

        // and processors automatically write their logs whenever they finish a windowsize, so we don't need
        // to explictly tell them to do so.
    }

    @Override
    public void endSimulationAction() {
        // we've been logging all the way along, but probably good to signal to all Processors that we're done and
        // write their final logs.
        Map<Integer, DescriptiveStatistics> statisticsMap = new HashMap<Integer, DescriptiveStatistics>();
        for (TimeAveragedWindowProcessor proc : this.windowProcessorList) {
            statisticsMap.put(proc.getWindowSize(), proc.calculateStatisticsAndFinalizeLogs());
        }
        this.logSummaryStatisticsAcrossWindows(statisticsMap);

    }

    @Override
    public void finalizeObservation() {
        // signal to processors to close their logs.
        for (TimeAveragedWindowProcessor proc : this.windowProcessorList) {
            proc.finalizeObservation();
        }
        this.statPW.flush();
        this.statPW.close();
    }

    // calculate a full list of windows, from 20% of the length, on down to windows 3 ticks long,
    // unless the -L switch is present, and then only collect data across the five largest windows.
    // in descending powers of 2.  e.g., 10000, 5000, 2500, 1250, 625
    private List<Integer> calculateTimeAvWindows() {
        if (this.model.getModelConfiguration().getCollectLongTAWindowsOnly() == true) {
            log.info("Long Time Averaging Windows selected");
            return timeAveragingWindowUtil.getLongTimeAvWindows();
        } else {
            log.info("Full Time Averaging Windows selected");
            return timeAveragingWindowUtil.calculateTimeAvWindows();
        }
    }

    private void logSummaryStatisticsAcrossWindows(Map<Integer, DescriptiveStatistics> statsMap) {
        log.trace("entering logSummaryStatisticsAcrossWindows");
        this.statPW.write("Duration \t Num \t Mean \t StDev \t Min \t Max \n\n");
        for (Integer windowsize : statsMap.keySet()) {
            DescriptiveStatistics stats = statsMap.get(windowsize);
            StringBuffer sb = new StringBuffer();
            sb.append(windowsize + "\t");
            sb.append(stats.getN() + "\t");
            sb.append(stats.getMean() + "\t");
            sb.append(stats.getStandardDeviation() + "\t");
            sb.append(stats.getMin() + "\t");
            sb.append(stats.getMax() + "\n");
            this.statPW.write(sb.toString());
        }
    }

}
