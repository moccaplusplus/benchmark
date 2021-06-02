package ppi.sensors.benchmark.cli.ext.impl;

import ppi.sensors.benchmark.cli.ext.PointMeshGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.ArrayList;
import java.util.List;

@ServiceName("square")
public class SquareMeshGeneratorImpl implements PointMeshGenerator {

    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final var result = new ArrayList<Point>();
        for (var y = 0; y <= sideLength; y += distance)
            for (var x = 0; x <= sideLength; x += distance)
                result.add(new Point(x, y));
        return result;
    }
}
