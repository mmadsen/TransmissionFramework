/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.observers;

import hep.aida.ref.Converter;
import hep.aida.ref.Histogram1D;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.ObserverConfiguration;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.interfaces.IStatistic;
import org.madsenlab.sim.tf.interfaces.IStatisticsObserver;
import org.madsenlab.sim.tf.interfaces.classification.IClass;
import org.madsenlab.sim.tf.interfaces.classification.IClassification;
import org.madsenlab.sim.tf.utils.ClassDescriptionComparator;
import org.madsenlab.sim.tf.utils.Log2HistogramBinUtil;

import java.io.PrintWriter;
import java.util.*;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Aug 8, 2010
 * Time: 3:19:51 PM
 */


public class GlobalClassCountObserver implements IStatisticsObserver<IClassification> {
    private ISimulationModel model;
    private Logger log;
    private Integer lastTimeIndexUpdated;
    private PrintWriter pw;
    private Map<Integer, Map<IClass, Integer>> histClassCount;
    private Histogram1D histo;
    private ObserverConfiguration config;


    public GlobalClassCountObserver(ISimulationModel m) {
        this.model = m;
        this.log = this.model.getModelLogger(this.getClass());
        this.histClassCount = new HashMap<Integer, Map<IClass, Integer>>();
        this.config = this.model.getModelConfiguration().getObserverConfigurationForClass(this.getClass());

        String countFreqLogFile = this.config.getParameter("global-class-counts");

        this.pw = this.model.getLogFileHandler().getFileWriterForPerRunOutput(countFreqLogFile);

        // initialize a histogram for counts with bins in powers of two
        Log2HistogramBinUtil histoBinUtil = new Log2HistogramBinUtil(this.model);
        double[] bins = histoBinUtil.getLog2HistogramBins();
        this.histo = new Histogram1D("Trait Count Histogram", bins);

    }

    @Override
    public void setParameterMap(Map<String, String> parameterMap) {

    }

    // we only want to start recording trait counts after the initial transient behavior decays and we reach equilibrium
    public void updateStatistics(IStatistic<IClassification> stat) {
        log.trace("entering updateStatistics");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getMixingtime()) {
            this.lastTimeIndexUpdated = stat.getTimeIndex();
            IClassification classification = stat.getTarget();
            Map<IClass, Integer> countMap = classification.getCurGlobalClassCounts();
            log.debug("updateStatistics - getting counts for time: " + this.lastTimeIndexUpdated + " : " + countMap);

            Integer time = this.model.getCurrentModelTime();
            this.histClassCount.put(time, countMap);
            log.debug("update statistics histClassCount: " + this.histClassCount);
        }
    }

    // we only want to start recording trait counts after the initial transient behavior decays and we reach equilibrium
    public void perStepAction() {
        log.trace("entering perStepAction");
        if (this.model.getCurrentModelTime() > this.model.getModelConfiguration().getMixingtime()) {
            //log.trace("histTraitFreq: " + this.histTraitFreq);
            this.printFrequencies();

            if (this.model.getCurrentModelTime() == this.model.getModelConfiguration().getSimlength() - 1) {
                Map<IClass, Integer> countMap = this.histClassCount.get(this.model.getCurrentModelTime());

                for (IClass cz : countMap.keySet()) {
                    this.histo.fill((double) countMap.get(cz));
                }
                Converter conv = new Converter();
                log.info(conv.toString(this.histo));
            }

            Integer tick = this.model.getCurrentModelTime();
            // Every 100 ticks, write historical data to disk and flush it to keep memory usage and performance reasonable.
            if (tick % 100 == 0) {
                this.logFrequencies();
                this.histClassCount.clear();
            }
        }
    }

    public void endSimulationAction() {
        log.trace("entering endSimulationAction");
        // this is only required if the simulation length is not evenly divisible by 100
        this.logFrequencies();

    }

    public void finalizeObservation() {
        log.trace("entering finalizeObservation");
        this.pw.flush();
        this.pw.close();
    }

    private void printFrequencies() {
        Integer time = this.model.getCurrentModelTime();
        log.debug("histClassCount: " + this.histClassCount);

        Map<IClass, Integer> countMap = this.histClassCount.get(time);
        log.debug("printCounts - getting counts for time: " + time + " : " + countMap);

        StringBuffer sb = prepareCountLogString(time, countMap);
        log.debug(sb.toString());

    }

    private StringBuffer prepareCountLogString(Integer time, Map<IClass, Integer> countMap) {
        //log.debug("prepare string: freqMap: " + freqMap);
        Integer numNonZeroClasses = 0;
        Integer totalOfClassCounts = 0;
        StringBuffer sb = new StringBuffer();
        Set<IClass> keys = countMap.keySet();
        List<IClass> sortedKeys = new ArrayList<IClass>(keys);
        Collections.sort(sortedKeys, new ClassDescriptionComparator());
        sb.append(time);
        sb.append(",");
        for (IClass cz : sortedKeys) {
            Integer count = countMap.get(cz);
            sb.append(cz.getClassIDWithinClassification());
            sb.append(":");
            sb.append(count);
            sb.append(",");
            if (count != 0) {
                numNonZeroClasses++;
            }
            totalOfClassCounts += count;
        }
        sb.append("tot:");
        sb.append(totalOfClassCounts);
        sb.append(",numclasses:");
        sb.append(numNonZeroClasses);
        return sb;
    }


    private void logFrequencies() {
        log.trace("entering logFrequencies");
        Set<Integer> keys = this.histClassCount.keySet();
        List<Integer> sortedKeys = new ArrayList<Integer>(keys);
        Collections.sort(sortedKeys);
        for (Integer time : sortedKeys) {
            Map<IClass, Integer> countMap = this.histClassCount.get(time);
            StringBuffer sb = prepareCountLogString(time, countMap);
            sb.append('\n');
            this.pw.write(sb.toString());
            //this.pw.flush();
        }

    }
}
