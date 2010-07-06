package org.mmadsen.sim.tf.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.mmadsen.sim.tf.interfaces.IAgent;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.interfaces.ITrait;
import org.mmadsen.sim.tf.models.AbstractSimModel;
import org.mmadsen.sim.tf.models.BasicSimulationModel;
import org.mmadsen.sim.tf.traits.UnstructuredTrait;

/**
 * Created by IntelliJ IDEA.
 * User: mark
 * Date: Jul 2, 2010
 * Time: 1:21:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicModelModule implements Module {
    public void configure(Binder binder) {
        binder.bind(ISimulationModel.class)
                .to(BasicSimulationModel.class)
                .in(Singleton.class);
        binder.bind(ITrait.class)
                .to(UnstructuredTrait.class);
        //binder.bind(IAgent.class)
                //.to();
    }
}
