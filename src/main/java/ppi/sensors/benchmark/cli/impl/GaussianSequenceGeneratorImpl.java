package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.util.ServiceName;

/**
 *
 */
@ServiceName("gaussian")
public class GaussianSequenceGeneratorImpl extends RandomSequenceGeneratorImpl {

    @Override
    protected double nextDouble() {
        return random.nextGaussian();
    }
}
