/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.mmadsen.sim.tf.models;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.mmadsen.sim.tf.interfaces.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 2:51:57 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractSimModel implements ISimulationModel
{
    protected Logger log;
    protected Integer currentTime = 0;
    @Inject public
    Provider<IAgent> agentProvider;
    @Inject public
    Provider<ITrait> traitProvider;
    @Inject public Provider<ITraitDimension> dimensionProvider;
    @Inject public Provider<IPopulation> populationProvider;
    protected IPopulation population;

    IPopulation agentPopulation;

    public AbstractSimModel() {
        log = Logger.getLogger(this.getClass());
        log.trace("log4j configured and ready");

    }



    public IPopulation getPopulation() {
        return this.population;
    }

    public Integer getCurrentModelTime() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Logger getModelLogger(Class classToLog) {
        return Logger.getLogger(classToLog);
    }

    public void initializePopulation() {
        this.population = populationProvider.get();
    }


    public Provider<ITrait> getTraitProvider() {
        return traitProvider;
    }

    public Provider<ITraitDimension> getTraitDimensionProvider() {
        return dimensionProvider;
    }

}
