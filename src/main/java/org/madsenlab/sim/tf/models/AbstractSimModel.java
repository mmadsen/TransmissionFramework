/*
 * Copyright (c) 2011.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.models;

import cern.jet.random.Uniform;
import cern.jet.random.engine.MersenneTwister;
import cern.jet.random.engine.RandomEngine;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.GlobalModelConfiguration;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.Properties;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSimModel implements ISimulationModel {
    protected Logger log;
    protected Integer currentTime = 0;
    @Inject
    protected
    Provider<IAgent> agentProvider;
    @Inject
    protected
    Provider<ITrait> traitProvider;
    @Inject
    protected Provider<ITraitDimension> dimensionProvider;
    @Inject
    protected Provider<IPopulation> populationProvider;
    @Inject
    protected Provider<IDeme> demeProvider;
    @Inject
    protected Provider<IInteractionTopology> topologyProvider;
    @Inject
    protected ILogFiles logFileHandler;

    protected IPopulation population;
    protected IInteractionTopology topology;

    protected RandomEngine rngGenerator;
    protected Uniform uniformDist;

    protected IPopulation agentPopulation;
    protected Integer lengthSimulation;
    protected GlobalModelConfiguration params;
    protected Properties modelProperties;

    public AbstractSimModel() {
        log = Logger.getLogger(this.getClass());
        log.trace("log4j configured and ready");

    }



    public IPopulation getPopulation() {
        return this.population;
    }

    public IInteractionTopology getInteractionTopology() {
        return this.topology;
    }

    public Integer getUniformRandomInteger(Integer ceiling) {
        return this.uniformDist.nextIntFromTo(0, ceiling);
    }

    public Double getUniformDouble() {
        return this.uniformDist.nextDoubleFromTo(0,1);
    }

    public Integer getCurrentModelTime() {
        return this.currentTime;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Logger getModelLogger(Class classToLog) {
        return Logger.getLogger(classToLog);
    }

    public void initializeRNG(Boolean reproducibleStream) {
        if (reproducibleStream) {
            this.rngGenerator = new MersenneTwister();
        } else {
            this.rngGenerator = new MersenneTwister(new java.util.Date());
        }
        this.uniformDist = new Uniform(this.rngGenerator);
    }

    public void initializeProviders() {
        this.population = populationProvider.get();
        this.topology = topologyProvider.get();

    }


    public Provider<ITrait> getTraitProvider() {
        return traitProvider;
    }

    public Provider<ITraitDimension> getTraitDimensionProvider() {
        return dimensionProvider;
    }

    public Provider<IDeme> getDemeProvider() {
        return demeProvider;
    }

    // TODO:  Deprecate this from all unit tests once a real simulation is fully complete - redo the test fixture model
    public void incrementModelTime() {
        synchronized(this.currentTime) {
            this.currentTime++;
        }
    }

    public ILogFiles getLogFileHandler() {
        return this.logFileHandler;
    }

    public GlobalModelConfiguration getModelConfiguration() {
        return this.params;
    }

    public void run() {
        log.info("Beginning simulation run");
        while(this.currentTime < this.lengthSimulation) {
            // first perform a model step, then allow observations to be recorded as desired
            this.modelStep();
            this.modelObservations();
            this.incrementModelTime();
        }

        log.info("Simulation run completed at time step: " + this.currentTime);
        this.modelFinalize();
    }

    public abstract void modelFinalize();



    public abstract void modelObservations();

    public abstract void modelStep();

    protected void loadPropertiesToConfig() {
        Set<String> propNames = this.modelProperties.stringPropertyNames();
        for(String propName: propNames) {
            this.params.setProperty(propName, this.modelProperties.getProperty(propName));
        }

        log.debug("params: " + this.params);
    }
}
