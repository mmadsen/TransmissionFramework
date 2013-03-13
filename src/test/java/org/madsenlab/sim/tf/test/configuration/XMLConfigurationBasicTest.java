/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.test.configuration;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.inject.Inject;
import net.sf.json.JSONObject;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.madsenlab.sim.tf.config.ModelConfiguration;
import org.madsenlab.sim.tf.config.ObserverConfiguration;
import org.madsenlab.sim.tf.config.PopulationConfiguration;
import org.madsenlab.sim.tf.interfaces.ISimulationModel;
import org.madsenlab.sim.tf.test.util.MultidimensionalAgentModule;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 6/21/12
 * Time: 10:22 AM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class XMLConfigurationBasicTest extends MultidimensionalAgentModule {
    @Unit
    @Inject
    private ISimulationModel model;
    private Logger log;


    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
        model.initializeProviders();
        // specifically we don't want to see repeatable random numbers here
        model.initializeRNG(false);
    }

    @After
    public void cleanUp() throws Exception {
        model.getPopulation().clearAgentPopulation();
    }

    private XMLConfiguration openConfigurationFile() {
        XMLConfiguration config = null;
        try {
            config = new XMLConfiguration("src/test/config/model-configuration-test.xml");
            log.info("loaded XML file");

        } catch (Exception e) {
            log.error("exception: " + e.getMessage());
        }
        return config;
    }

    private ModelConfiguration initializeModelConfiguration(XMLConfiguration config) {
        ModelConfiguration mc = new ModelConfiguration();
        mc.setSimlength(config.getInt("model.simlength"));
        mc.setMixingtime(config.getInt("model.mixingtime"));
        mc.setDynamicsclass(config.getString("model.dynamicsclass"));
        return mc;
    }

    private ModelConfiguration initializePopulationConfiguration(XMLConfiguration config, ModelConfiguration mc) {
        PopulationConfiguration pc = new PopulationConfiguration();
        pc.setAgentclass(config.getString("model.population.agentclass"));
        pc.setBuilderclass(config.getString("model.population.builderclass"));
        pc.setTopologyclass(config.getString("model.population.topologyclass"));
        pc.setNumagents(config.getInt("model.population.numagents"));
        mc.setPopulation(pc);

        return mc;
    }


    @Test
    public void testLoadingConfiguration() throws Exception {
        log.info("entering testLoadingConfiguration");
        XMLConfiguration config = this.openConfigurationFile();

        int obsLengthSim = config.getInt("model.simlength");
        int expectedLengthSim = 10000;

        log.info("expected simlength: " + expectedLengthSim + " observed: " + obsLengthSim);
        assertEquals(obsLengthSim, expectedLengthSim);
        log.info("exiting testLoadingConfiguration");

    }


    @Test
    public void testConstructingNonNestedConfigurationClasses() throws Exception {
        log.info("entering testConstructingNonNestedConfigurationClasses");
        XMLConfiguration config = this.openConfigurationFile();
        ModelConfiguration mc = this.initializeModelConfiguration(config);


        int expectedMixingTime = 1000;
        int obsMixingTime = mc.getMixingtime();

        log.info("expected mixing time: " + expectedMixingTime + " observed: " + obsMixingTime);

        assertEquals(expectedMixingTime, obsMixingTime);
        log.info("exiting testConstructingNonNestedConfigurationClasses");
    }

    @Test
    public void testConstructingNestedConfigurations() throws Exception {
        log.info("entering testConstructingNestedConfigurations");
        XMLConfiguration config = this.openConfigurationFile();
        ModelConfiguration mc = this.initializeModelConfiguration(config);
        ModelConfiguration loadedMC = this.initializePopulationConfiguration(config, mc);

        int expectedNumAgents = 2000;
        int obsNumAgents = loadedMC.getPopulation().getNumagents();

        log.debug(JSONObject.fromObject(loadedMC).toString());

        log.info("expected numagents from population: " + expectedNumAgents + " observed: " + obsNumAgents);

        assertEquals(expectedNumAgents, obsNumAgents);
        log.info("exiting testConstructingNestedConfigurations");
    }

    @Test
    public void testConfigurationMultipleObservers() throws Exception {
        log.info("entering testConfigurationMultipleObservers");
        XMLConfiguration config = this.openConfigurationFile();
        //ModelConfiguration mc = this.initializeModelConfiguration(config);
        List<ObserverConfiguration> observers = new ArrayList<ObserverConfiguration>();

        List<HierarchicalConfiguration> obsConfigList = config.configurationsAt("observation.observers.observer");
        for (HierarchicalConfiguration sub : obsConfigList) {
            String obsClass = sub.getString("observerclass");
            ObserverConfiguration obsConfig = new ObserverConfiguration();
            obsConfig.setObserverClass(obsClass);

            List<HierarchicalConfiguration> paramConfigList = sub.configurationsAt("parameters.parameter");
            for (HierarchicalConfiguration param : paramConfigList) {
                String name = param.getString("name");
                String value = param.getString("value");
                log.info("parameter: " + name + " value: " + value);
                obsConfig.addParameter(name, value);
            }


            observers.add(obsConfig);
        }

//        for(ObserverConfiguration obsConfig: observers) {
//            log.info(JSONObject.fromObject(obsConfig).toString());
//        }


        int expectedNumObservers = 6;
        int obsNumObservers = observers.size();

        log.info("expected num observer configs: " + expectedNumObservers + " observed: " + obsNumObservers);
        assertEquals("Did not find the expected number of observer configuration blocks", expectedNumObservers, obsNumObservers);

        String samplesizeStr = null;
        for (ObserverConfiguration conf : observers) {
            String cz = conf.getObserverClass();
            if (cz.contains("Ewens")) {
                samplesizeStr = conf.getParameter("samplesize");
            }
        }

        Integer samplesize = Integer.parseInt(samplesizeStr);
        Integer expectedSampleSize = 100;

        log.info("expected Ewens sample size: " + expectedSampleSize + " observed: " + samplesize);
        assertEquals("Did not find the expected Ewens sample size from the observer configuration", expectedSampleSize, samplesize);

        log.info("exiting testConfigurationMultipleObservers");
    }

    @Test
    public void testConfigurationWithAttributes() {
        log.info("entering testConfigurationWithAttributes");
        XMLConfiguration config = this.openConfigurationFile();
        List<String> expectedRulesetNames = new ArrayList<String>();
        List<String> observedRulesetNames = new ArrayList<String>();

        expectedRulesetNames.add("IAMutationOrCopyRandomNeighbor");
        expectedRulesetNames.add("IAMutationOrConformistCopyNeighbors");

        List<HierarchicalConfiguration> ruleConfigList = config.configurationsAt("model.population.rulesets.ruleset");
        for (HierarchicalConfiguration sub : ruleConfigList) {
            String ruleID = sub.getString("[@id]");
            String ruleName = sub.getString("[@name]");
            observedRulesetNames.add(ruleName);

            log.info("ruleset id: " + ruleID + " name: " + ruleName);
        }

        // for the two lists to be identical in their elements, the observedRulesetNames should not change
        // as the result of this call, and we will get a FALSE
        Boolean incompleteOverlap = observedRulesetNames.retainAll(expectedRulesetNames);

        assertFalse("One or more observed values were different than expected ruleset names", incompleteOverlap);
        log.info("exiting testConfigurationWithAttributes");
    }

}
