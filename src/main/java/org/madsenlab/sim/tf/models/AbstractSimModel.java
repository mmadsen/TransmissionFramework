/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
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
import org.madsenlab.sim.tf.interfaces.*;

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
    public
    Provider<IAgent> agentProvider;
    @Inject
    public
    Provider<ITrait> traitProvider;
    @Inject
    public Provider<ITraitDimension> dimensionProvider;
    @Inject
    public Provider<IPopulation> populationProvider;
    @Inject
    public Provider<IDeme> demeProvider;
    @Inject
    public Provider<IInteractionTopology> topologyProvider;
    protected IPopulation population;
    protected IInteractionTopology topology;

    protected RandomEngine rngGenerator;
    protected Uniform uniformDist;

    IPopulation agentPopulation;

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

    public Integer getCurrentModelTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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


}
