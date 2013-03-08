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
 * Date: 3/4/13
 * Time: 12:45 PM
 */

public class ObserverConfiguration {
    private String observerClass;
    private Map<String, String> parameterMap;

    public String getObserverClass() {
        return observerClass;
    }

    public void setObserverClass(String observerClass) {
        this.observerClass = observerClass;
    }

    public void addParameter(String name, String value) {
        this.parameterMap.put(name, value);
    }

    public String getParameter(String name) {
        return this.parameterMap.get(name);
    }

    public ObserverConfiguration() {
        this.parameterMap = new HashMap<String, String>();
    }


}
