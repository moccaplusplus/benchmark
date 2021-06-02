package ppi.sensors.benchmark.cli.ext;

import ppi.sensors.benchmark.cli.model.Point;

import java.util.List;

public interface PointMeshGenerator {
    List<Point> createMesh(double distance, int sideLength);
}
