/*
 * Copyright (c) 2013.  Mark E. Madsen <mark@madsenlab.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enumeration of types for IAgentTag classes
 * <p/>
 * <p/>
 * User: mark
 * Date: 4/6/11
 * Time: 4:25 PM
 */
public enum AgentTagType {
    DEME("DEME"),
    AGENT_RULE_TYPE("AGENT_RULE_TYPE");

    private final String tagType;

    private static final Map<String, AgentTagType> stringToEnum = new ConcurrentHashMap<String, AgentTagType>();

    static {
        for (AgentTagType set : values()) {
            stringToEnum.put(set.tagType, set);
        }
    }

    private AgentTagType(String setting) {
        this.tagType = setting;
    }

    public String toString() {
        return this.tagType;
    }

    public static AgentTagType fromString(String setting) {
        return stringToEnum.get(setting);
    }
}
