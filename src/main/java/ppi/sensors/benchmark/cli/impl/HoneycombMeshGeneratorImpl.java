package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.PointMeshGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator siatki punktów opartej na sześciokątach foremnych (aka "plaster miodu").
 */
@ServiceName("honeycomb")
public class HoneycombMeshGeneratorImpl implements PointMeshGenerator {

    /**
     * Zwraca listę punktów tworzących siatkę opartą na sześciokątach foremnych (aka "plaster miodu").
     *
     * @param distance   odległość pomiędzy node'ami siatki - długość boku sześciokąta foremnego.
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return lista punktów tworzących siatkę.
     */
    @Override
    public List<Point> createMesh(double distance, int sideLength) {
        final List<Point> result = new ArrayList<>();
        final double h = distance * Math.sqrt(3) / 2;
        boolean odd = false;
        for (double y = 0.0; y <= sideLength; y += h, odd = !odd) {
            int i = odd ? 1 : 2;
            for (double x = odd ? distance / 2.0 : 0.0; x <= sideLength; x += distance, i++) {
                if (i % 3 == 0) continue;
                result.add(new Point(x, y));
            }
        }
        return result;
    }
}
