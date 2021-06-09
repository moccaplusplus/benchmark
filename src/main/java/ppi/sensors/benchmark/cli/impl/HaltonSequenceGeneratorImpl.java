package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;

@ServiceName("halton")
public class HaltonSequenceGeneratorImpl implements PointSequenceGenerator {

    private int counter = 0;

    @Override
    public List<Point> createSequence(int count, int sideLength) {
        return generate(() -> nextPoint(sideLength)).sequential().limit(count).collect(toList());
    }

    public Point nextPoint(int sideLength) {
        final int index = counter++;
        return new Point(
                haltonNumber(index, 2) * sideLength,
                haltonNumber(index, 3) * sideLength);
    }

    /**
     * Implementation based on pseudocode given on https://en.wikipedia.org/wiki/Halton_sequence
     *
     * @param index - index in halton sequence.
     * @param base  - base number (should be prime).
     * @return a number in halton sequence for given index and base.
     */
    private double haltonNumber(int index, int base) {
        double result = 0.0;
        double factor = 1.0;

        while (index > 0) {
            factor /= base;
            result += factor * (index % base);
            index /= base;
        }

        return result;
    }
}
