package ppi.sensors.benchmark.cli.ext;

import ppi.sensors.benchmark.cli.model.Point;

import java.util.List;

public interface PointSequenceGenerator {
    List<Point> createSequence(int count, int sideLength);
}
