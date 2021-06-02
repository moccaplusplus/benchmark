package ppi.sensors.benchmark.cli.ext.impl;

import ppi.sensors.benchmark.cli.ext.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;

@ServiceName("random")
public class RandomSequenceGeneratorImpl implements PointSequenceGenerator {

    private final Random random;

    public RandomSequenceGeneratorImpl() {
        random = new Random();
        random.setSeed(Instant.now().toEpochMilli());
    }

    @Override
    public List<Point> createSequence(int count, int sideLength) {
        return generate(() -> nextPoint(sideLength)).sequential().limit(count).collect(toList());
    }

    public Point nextPoint(int sideLength) {
        return new Point(random.nextDouble() * sideLength, random.nextDouble() * sideLength);
    }
}
