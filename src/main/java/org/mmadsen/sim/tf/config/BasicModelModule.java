package org.mmadsen.sim.tf.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.mmadsen.sim.tf.interfaces.ISimulationModel;
import org.mmadsen.sim.tf.models.BasicModel;

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
                .to(BasicModel.class)
                .in(Singleton.class);
    }
}
