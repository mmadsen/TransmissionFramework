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
 * Date: 3/5/13
 * Time: 3:14 PM
 */

public class RulesetConfiguration {
    private String rulesetName;
    List<RuleConfiguration> ruleList;
    private Integer rulesetID;

    public Integer getRulesetID() {
        return rulesetID;
    }

    public void setRulesetID(Integer rulesetID) {
        this.rulesetID = rulesetID;
    }

    public String getRulesetName() {
        return rulesetName;
    }

    public void setRulesetName(String rulesetName) {
        this.rulesetName = rulesetName;
    }

    public RulesetConfiguration() {
        this.ruleList = new ArrayList<RuleConfiguration>();
    }

    public void addRule(RuleConfiguration rule) {
        this.ruleList.add(rule);
    }

    public List<RuleConfiguration> getRuleList() {
        return this.ruleList;
    }
}
