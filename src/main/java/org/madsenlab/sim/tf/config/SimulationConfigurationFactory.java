/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.config;

import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.utils.ObserverTargetType;
import org.madsenlab.sim.tf.utils.RealTraitIntervalPredicate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/6/13
 * Time: 1:22 PM
 */

public class SimulationConfigurationFactory {
    private ISimulationModel model;
    private Logger log;
    private XMLConfiguration config;
    private ModelConfiguration mc;

    public SimulationConfigurationFactory(ISimulationModel m) {
        this.model = m;
        // we need to read the config file before we initialize logging....
        //this.log = this.model.getModelLogger(this.getClass());

    }

    public ModelConfiguration getModelConfiguration() {
        return this.mc;
    }

    public void processConfigurationFile(String pathname) {
        this.config = openConfigurationFile(pathname);

        this.initializeModelConfiguration();
        this.processPopulationConfiguration();
        this.processObservation();
        this.processObservers();
        this.processTraitDimensions();
        this.processClassifications();
    }

    private void initializeModelConfiguration() {
        this.mc = new ModelConfiguration();
        mc.setSimlength(this.config.getInt("model.simlength"));
        mc.setMixingtime(this.config.getInt("model.mixingtime"));
        mc.setDynamicsclass(this.config.getString("model.dynamicsclass"));
        mc.setModelName(this.config.getString("model.modelname"));

    }

    private void processPopulationConfiguration() {
        PopulationConfiguration pc = new PopulationConfiguration();
        pc.setAgentclass(config.getString("model.population.agentclass"));
        pc.setBuilderclass(config.getString("model.population.builderclass"));
        pc.setTopologyclass(config.getString("model.population.topologyclass"));
        pc.setNumagents(config.getInt("model.population.numagents"));
        this.processRulesets(pc);

        mc.setPopulation(pc);
    }

    private void processRulesets(PopulationConfiguration pc) {
        List<HierarchicalConfiguration> ruleConfigList = config.configurationsAt("model.population.rulesets.ruleset");
        for (HierarchicalConfiguration sub : ruleConfigList) {
            int rulesetID = sub.getInt("[@id]");
            String ruleName = sub.getString("[@name]");


            RulesetConfiguration rsc = new RulesetConfiguration();
            rsc.setRulesetName(ruleName);
            rsc.setRulesetID(rulesetID);

            // now recurse into the rule list in the rule set
            List<HierarchicalConfiguration> rules = sub.configurationsAt("rule");
            for (HierarchicalConfiguration rule : rules) {
                int ruleID = rule.getInt("[@id]");
                String ruleClass = rule.getString("ruleclass");

                RuleConfiguration rc = new RuleConfiguration();
                rc.setRuleID(ruleID);
                rc.setRuleClass(ruleClass);

                if (rule.containsKey("subrule-of")) {
                    int subruleOf = rule.getInt("subrule-of");
                    rc.setSubruleOf(subruleOf);
                }

                List<HierarchicalConfiguration> paramConfigList = rule.configurationsAt("parameters.parameter");
                for (HierarchicalConfiguration param : paramConfigList) {
                    String name = param.getString("name");
                    String value = param.getString("value");
                    //log.debug("parameter: " + name + " value: " + value);
                    rc.addParameter(name, value);
                }
                // add completed rule to the ruleset
                rsc.addRule(rc);
            }
            // add completed ruleset to the population object
            pc.addRuleset(rsc);
        }
        // processed all rulesets
    }

    private void processObservers() {
        List<HierarchicalConfiguration> obsConfigList = this.config.configurationsAt("observation.observers.observer");
        for (HierarchicalConfiguration sub : obsConfigList) {
            String obsClass = sub.getString("observerclass");
            ObserverConfiguration obsConfig = new ObserverConfiguration();
            obsConfig.setObserverClass(obsClass);

            String targetType = sub.getString("target");
            obsConfig.setTargetType(ObserverTargetType.fromString(targetType));

            List<HierarchicalConfiguration> paramConfigList = sub.configurationsAt("parameters.parameter");
            for (HierarchicalConfiguration param : paramConfigList) {
                String name = param.getString("name");
                String value = param.getString("value");
                //log.debug("parameter: " + name + " value: " + value);
                obsConfig.addParameter(name, value);
            }
            this.mc.addObserverConfiguration(obsConfig);
        }
    }

    private void processObservation() {
        String logParentDirectory = this.config.getString("observation.logging.parent-directory");
        this.mc.setLogParentDirectory(logParentDirectory);
        //log.debug("logParentDirectory: " + logParentDirectory);
    }

    private void processTraitDimensions() {
        List<HierarchicalConfiguration> traitDimensionList = this.config.configurationsAt("model.traitspace.traitdimension");
        for (HierarchicalConfiguration dim : traitDimensionList) {
            TraitDimensionConfiguration tdc = new TraitDimensionConfiguration();
            int dimID = dim.getInt("[@id]");
            tdc.setDimensionID(dimID);

            String dimName = dim.getString("dimensionname");
            String dimType = dim.getString("dimensiontype");
            String traitFactory = dim.getString("variationmodel.factory");
            String initGenMethod = dim.getString("variationmodel.initial-trait-generator-method");
            tdc.setDimensionName(dimName);
            tdc.setDimensionType(dimType);
            tdc.setInitialTraitGeneratorMethodName(initGenMethod);
            tdc.setVariationModelFactoryClass(traitFactory);


            List<HierarchicalConfiguration> paramConfigList = dim.configurationsAt("variationmodel.parameters.parameter");
            for (HierarchicalConfiguration param : paramConfigList) {
                String name = param.getString("name");
                String value = param.getString("value");
                //log.debug("parameter: " + name + " value: " + value);
                tdc.addInitialTraitGeneratorParameter(name, value);
            }

            // done with this dimension
            this.mc.addTraitDimensionConfiguration(tdc);
        }
        // done
    }


    private void processClassifications() {
        List<HierarchicalConfiguration> classificationList = this.config.configurationsAt("classifications.classification");

        for (HierarchicalConfiguration classification : classificationList) {
            ClassificationConfiguration cc = new ClassificationConfiguration();

            String cname = classification.getString("name");
            cc.setClassificationName(cname);
            List<HierarchicalConfiguration> classDimList = classification.configurationsAt("dimensions.dimension");

            for (HierarchicalConfiguration classDim : classDimList) {
                int mapsTraitDim = classDim.getInt("maps-traitdimension");
                String modetype = classDim.getString("modetype");
                ClassificationDimensionConfiguration cdc = new ClassificationDimensionConfiguration();
                cdc.setModeType(modetype);
                cdc.setTraitDimensionTracked(mapsTraitDim);

                if (modetype.equals("SPECIFIED")) {
                    List<HierarchicalConfiguration> predicateList = classDim.configurationsAt("modelist.mode");
                    for (HierarchicalConfiguration predConfig : predicateList) {
                        String predName = predConfig.getString("name");
                        Map<String, String> paramMap = new HashMap<String, String>();

                        // now get mode parameters
                        List<HierarchicalConfiguration> predParamConfig = predConfig.configurationsAt("parameters.parameter");
                        for (HierarchicalConfiguration predParam : predParamConfig) {
                            paramMap.put(predParam.getString("name"), predParam.getString("value"));
                        }

                        RealTraitIntervalPredicate pred = this.parsePredicate(paramMap.get("lowerBound"), paramMap.get("isClosedLower"), paramMap.get("upperBound"), paramMap.get("isClosedUpper"), predName);
                        cdc.addModePredicate(pred);
                    }
                } else {
                    // modetype = RANDOM
                    int numModes = classDim.getInt("nummodes");
                    cdc.setNumberModesForRandomModeType(numModes);
                }
                cc.addClassificationDimensionConfiguration(cdc);
                //log.info(JSONObject.fromObject(cc).toString());
            }
            this.mc.addClassificationConfiguration(cc);
        }
    }

    private RealTraitIntervalPredicate parsePredicate(String lowerBoundStr, String isClosedLowerStr, String upperBoundStr, String isClosedUpperStr, String name) {
        Double lowerBound = Double.parseDouble(lowerBoundStr);
        Boolean isClosedLower = Boolean.parseBoolean(isClosedLowerStr);
        Double upperBound = Double.parseDouble(upperBoundStr);
        Boolean isClosedUpper = Boolean.parseBoolean(isClosedUpperStr);

        return new RealTraitIntervalPredicate(lowerBound, isClosedLower, upperBound, isClosedUpper, name);
    }

    private XMLConfiguration openConfigurationFile(String pathname) {
        XMLConfiguration config = null;
        try {
            config = new XMLConfiguration(pathname);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        return config;
    }


}
