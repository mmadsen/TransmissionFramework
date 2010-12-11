/*
 * Copyright (c) 2010.  Mark E. Madsen <mark@mmadsen.org>
 *
 * This work is licensed under the terms of the Creative Commons-GNU General Public Llicense 2.0, as "non-commercial/sharealike".  You may use, modify, and distribute this software for non-commercial purposes, and you must distribute any modifications under the same license.
 *
 * For detailed license terms, see:
 * http://creativecommons.org/licenses/GPL/2.0/
 */

package org.madsenlab.sim.tf.interfaces;


import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 9, 2010
 * Time: 5:19:11 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IAgentTag {

    public void setSimulationModel(ISimulationModel m);

    public String getTagName();

    public void setTagName(String tagname);


    public List<IAgent> getCurAgentsTagged();

    public void registerAgent(IAgent agent);

    public void unregisterAgent(IAgent agent);


    public Integer curAgentCount();

    public Map<Integer, Integer> getAgentCountHistory();

}
