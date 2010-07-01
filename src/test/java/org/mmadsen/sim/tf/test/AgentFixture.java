package org.mmadsen.sim.tf.test;

import org.mmadsen.sim.tf.interfaces.IAgent;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 4:08:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgentFixture implements IAgent {

    private String agentID;

    public AgentFixture(Integer agentID) {
        this.agentID = new String("TestAgent" + agentID);

    }


    public String getAgentID() {
        return this.agentID;
    }
}
