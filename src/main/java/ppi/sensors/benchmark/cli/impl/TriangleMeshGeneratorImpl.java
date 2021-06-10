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

    /**
     * Zwraca listę punktów tworzących siatkę opartą na trójkątach równobocznych.
     *
     * @param distance odległość pomiędzy node'ami siatki - długość boku trójkata równobocznego.
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return lista punktów tworzących siatkę.
     */
    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final var result = new ArrayList<Point>();
        final var h = distance * Math.sqrt(3) / 2;
        var odd = false;
        for (var y = 0.0; y <= sideLength; y += h, odd = !odd)
            for (var x = odd ? distance / 2.0 : 0.0; x <= sideLength; x += distance)
                result.add(new Point(x, y));
        return result;
    }
}
