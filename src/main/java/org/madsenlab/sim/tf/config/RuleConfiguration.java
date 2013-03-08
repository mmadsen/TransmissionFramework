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
 * Time: 2:42 PM
 */

public class RuleConfiguration {
    private Integer ruleID;
    private String ruleClass;
    private Integer subruleOf;
    private Map<String, String> parameterMap;

    public RuleConfiguration() {
        this.parameterMap = new HashMap<String, String>();
    }

    public Integer getRuleID() {
        return ruleID;
    }

    public void setRuleID(Integer ruleID) {
        this.ruleID = ruleID;
    }

    public String getRuleClass() {
        return ruleClass;
    }

    public void setRuleClass(String ruleClass) {
        this.ruleClass = ruleClass;
    }

    public Integer getSubruleOf() {
        return subruleOf;
    }

    public void setSubruleOf(Integer subruleOf) {
        this.subruleOf = subruleOf;
    }

    public void addParameter(String name, String value) {
        this.parameterMap.put(name, value);
    }

    public String getParameter(String name) {
        return this.parameterMap.get(name);
    }

}
