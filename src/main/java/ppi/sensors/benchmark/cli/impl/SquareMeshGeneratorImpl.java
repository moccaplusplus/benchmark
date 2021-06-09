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

    /**
     * Zwraca listę punktów tworzących siatkę opartą na kwadratach.
     *
     * @param distance odległość pomiędzy node'ami siatki - długość boku kwadratu bazowego siatki.
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return lista punktów tworzących siatkę.
     */
    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final List<Point> result = new ArrayList<>();
        for (double y = 0.0; y <= sideLength; y += distance)
            for (double x = 0.0; x <= sideLength; x += distance)
                result.add(new Point(x, y));
        return result;
    }
}
