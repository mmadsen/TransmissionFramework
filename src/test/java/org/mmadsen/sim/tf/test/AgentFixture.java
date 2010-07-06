package org.mmadsen.sim.tf.test;

import com.google.inject.Inject;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.interfaces.ITraitDimension;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jun 30, 2010
 * Time: 4:08:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class AgentFixture implements IAgent {

    private String agentID;
    @Inject private ISimulationModel model;

    // used only when you can't do DI
    public void setSimulationModel(ISimulationModel model) {
        model = model;
    }

    public void setAgentID(String agentID) {
        this.agentID = new String("TestAgent" + agentID);
    }

    public String getAgentID() {
        return this.agentID;
    }

    public void adoptTrait(ITrait trait) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void adoptTrait(ITraitDimension dimension, ITrait trait) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addTraitDimension(ITraitDimension dimension) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
