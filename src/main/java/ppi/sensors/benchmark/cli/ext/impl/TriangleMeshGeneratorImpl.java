package ppi.sensors.benchmark.cli.ext.impl;

import ppi.sensors.benchmark.cli.ext.PointMeshGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.ArrayList;
import java.util.List;

@ServiceName("triangle")
public class TriangleMeshGeneratorImpl implements PointMeshGenerator {

    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final var result = new ArrayList<Point>();
        final var h = distance * Math.sqrt(3) / 2;
        var odd = false;
        for (var y = 0; y <= sideLength; y += h, odd = !odd)
            for (var x = odd ? distance / 2 : 0; x <= sideLength; x += distance)
                result.add(new Point(x, y));
        return result;
    }
}
