/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.config;

import java.util.HashMap;
import java.util.Map;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/6/13
 * Time: 9:52 AM
 */

public class TraitDimensionConfiguration {
    private Map<String, String> parameterMap;
    private String dimensionType;
    private String variationModelFactoryClass;
    private String initialTraitGeneratorMethodName;
    private Integer dimensionID;
    private String dimensionName;

    public String getDimensionName() {
        return dimensionName;
    }

    public void setDimensionName(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    public Integer getDimensionID() {
        return dimensionID;
    }

    public void setDimensionID(Integer dimensionID) {
        this.dimensionID = dimensionID;
    }

    public String getDimensionType() {
        return dimensionType;
    }

    public void setDimensionType(String dimensionType) {
        this.dimensionType = dimensionType;
    }

    public String getVariationModelFactoryClass() {
        return variationModelFactoryClass;
    }

    public void setVariationModelFactoryClass(String variationModelFactoryClass) {
        this.variationModelFactoryClass = variationModelFactoryClass;
    }

    public String getInitialTraitGeneratorMethodName() {
        return initialTraitGeneratorMethodName;
    }

    public void setInitialTraitGeneratorMethodName(String initialTraitGeneratorMethodName) {
        this.initialTraitGeneratorMethodName = initialTraitGeneratorMethodName;
    }


    public TraitDimensionConfiguration() {
        this.parameterMap = new HashMap<String, String>();
    }

    public void addInitialTraitGeneratorParameter(String name, String value) {
        this.parameterMap.put(name, value);
    }

    public String getInitialTraitGeneratorParameter(String name) {
        return this.parameterMap.get(name);
    }


}
