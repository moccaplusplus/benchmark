package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.PointMeshGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator siatki punktów opartej na trójkątach równobocznych.
 */
@ServiceName("triangle")
public class TriangleMeshGeneratorImpl implements PointMeshGenerator {

    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final List<Point> result = new ArrayList<>();
        final double h = distance * Math.sqrt(3) / 2;
        boolean odd = false;
        for (double y = 0.0; y <= sideLength; y += h, odd = !odd)
            for (double x = odd ? distance / 2.0 : 0.0; x <= sideLength; x += distance)
                result.add(new Point(x, y));
        return result;
    }
}
