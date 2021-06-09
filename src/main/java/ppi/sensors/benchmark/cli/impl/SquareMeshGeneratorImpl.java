package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.PointMeshGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator siatki punktów opartej na kwadratach.
 */
@ServiceName("square")
public class SquareMeshGeneratorImpl implements PointMeshGenerator {

    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final List<Point> result = new ArrayList<>();
        for (double y = 0.0; y <= sideLength; y += distance)
            for (double x = 0.0; x <= sideLength; x += distance)
                result.add(new Point(x, y));
        return result;
    }
}
