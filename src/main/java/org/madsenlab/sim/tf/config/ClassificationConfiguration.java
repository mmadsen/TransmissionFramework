/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.config;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: 3/6/13
 * Time: 10:26 AM
 */

public class ClassificationConfiguration {

    private String classificationName;
    private List<ClassificationDimensionConfiguration> classDimConfigurations;

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }


    public ClassificationConfiguration() {
        this.classDimConfigurations = new ArrayList<ClassificationDimensionConfiguration>();
    }

    public void addClassificationDimensionConfiguration(ClassificationDimensionConfiguration cdc) {
        this.classDimConfigurations.add(cdc);
    }

    public List<ClassificationDimensionConfiguration> getClassificationDimensionConfigurations() {
        return this.classDimConfigurations;
    }

}
