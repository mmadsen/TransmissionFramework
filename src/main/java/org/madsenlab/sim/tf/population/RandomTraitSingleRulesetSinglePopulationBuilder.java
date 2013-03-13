/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.population;

import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.config.ModelConfiguration;
import org.madsenlab.sim.tf.interfaces.*;

import java.util.List;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 2/27/13
 * Time: 3:56 PM
 */

public class RandomTraitSingleRulesetSinglePopulationBuilder implements IInitialPopulationBuilder {
    private ISimulationModel model;
    private Logger log;
    private IPopulation initialPopulation;
    private ModelConfiguration mc;
    private Map<Integer, IActionRule> ruleMap;

    public RandomTraitSingleRulesetSinglePopulationBuilder(ISimulationModel model) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());

    }


    @Override
    public void setModelConfiguration(ModelConfiguration configuration) {
        this.mc = configuration;
    }

    @Override
    public IPopulation constructInitialPopulation(IPopulation pop) {
        this.initialPopulation = pop;
        IActionRule ruleset = this.ruleMap.get(1);
        List<ITraitDimension> dimensionList = this.model.getTraitDimensions();

        Integer numAgents = this.mc.getPopulation().getNumagents();

        for (Integer i = 0; i < numAgents; i++) {
            IAgent agent = this.initialPopulation.createAgent();
            agent.setAgentID(i.toString());

            // choose random traits
            // adopt the trait

        }


        return this.initialPopulation;
    }

    @Override
    public void setRulesets(Map<Integer, IActionRule> ruleMap) {
        this.ruleMap = ruleMap;
    }
}
