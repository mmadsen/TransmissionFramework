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
import org.madsenlab.sim.tf.config.TraitDimensionConfiguration;
import org.madsenlab.sim.tf.enums.InitialTraitCreationMethodType;
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
    private Map<Integer, ITraitDimension> traitDimensionMap;

    public RandomTraitSingleRulesetSinglePopulationBuilder(ISimulationModel model) {
        this.model = model;
        this.log = this.model.getModelLogger(this.getClass());

    }


    @Override
    public void setModelConfiguration(ModelConfiguration configuration) {
        this.mc = configuration;
    }

    @Override
    public IPopulation constructInitialPopulation(IPopulation pop, Map<Integer, ITraitDimension> tdm) {
        this.initialPopulation = pop;
        this.traitDimensionMap = tdm;

        IActionRule ruleset = this.ruleMap.get(1);
        List<ITraitDimension> dimensionList = this.model.getTraitDimensions();

        Integer numAgents = this.mc.getPopulation().getNumagents();
        log.debug("constructing population of " + numAgents + " agents");


        // Set up initial traits within each dimension given the configured methods
        for (Integer traitDimID : this.traitDimensionMap.keySet()) {
            ITraitDimension dimension = this.traitDimensionMap.get(traitDimID);
            TraitDimensionConfiguration dimConfig = this.mc.getTraitDimensionConfigurationForID(traitDimID);

            InitialTraitCreationMethodType creationType = dimConfig.getInitialTraitGeneratorMethodName();
            Map<String, String> initialTraitFactoryParameters = dimConfig.getInitialTraitGeneratorParameters();

            // initialize given the type
            Integer numTraits = 0;
            switch (creationType) {
                case UNIFORM:
                    numTraits = Integer.parseInt(initialTraitFactoryParameters.get("numtraits"));
                    dimension.getUniformTraitCollection(numTraits);
                    break;

                case UNIQUE_UNIFORM:
                    numTraits = Integer.parseInt(initialTraitFactoryParameters.get("numtraits"));
                    dimension.getUniqueUniformTraitCollection(numTraits);
                    break;

                case GAUSSIAN:

                    Double mean = Double.parseDouble(initialTraitFactoryParameters.get("mean"));
                    Double stdev = Double.parseDouble(initialTraitFactoryParameters.get("stdev"));
                    numTraits = Integer.parseInt(initialTraitFactoryParameters.get("numtraits"));
                    dimension.getGaussianTraitCollection(numTraits, mean, stdev);
                    break;
            }
        }

        // From the initial trait pool, assign random traits to initial agent population
        for (Integer i = 0; i < numAgents; i++) {
            IAgent agent = this.initialPopulation.createAgent();
            agent.setNumTraitDimensionsExpected(dimensionList.size());
            agent.setAgentID(i.toString());
            agent.addActionRule(ruleset);

            // choose random traits from each dimension, and adopt them.
            for (ITraitDimension dimension : dimensionList) {
                ITrait randomTrait = dimension.getRandomTraitFromDimension();
                randomTrait.adopt(agent);
            }

            // If we are using the "previous" mode of copying, then we need to prime the pump by ALSO copying
            // the initial traits to the "previous" data structures.
            agent.savePreviousStepTraits();

            agent.setAgentInitialized(Boolean.TRUE);

        }

        log.info("initial agent population: " + this.initialPopulation.getAgents().size());


        return this.initialPopulation;
    }


    @Override
    public void setRulesets(Map<Integer, IActionRule> ruleMap) {
        this.ruleMap = ruleMap;
    }
}
