package org.mmadsen.sim.tf.test;

import static org.junit.Assert.*;

import atunit.AtUnit;
import atunit.Container;
import atunit.Unit;
import com.google.inject.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * CLASS DESCRIPTION
 * <p/>
 * User: mark
 * Date: Sep 30, 2010
 * Time: 3:26:55 PM
 */

@RunWith(AtUnit.class)
@Container(Container.Option.GUICE)
public class PopulationDemeTest implements Module {
    private Logger log;

    @Before
    public void setUp() throws Exception {
        log = model.getModelLogger(this.getClass());
    }

    public void configure(Binder binder) {

    }
}
