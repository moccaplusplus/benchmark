package ppi.sensors.benchmark.cli;

import ppi.sensors.benchmark.cli.model.Point;

import java.util.List;

/**
 * Generator siatki punktów.
 * Używany do generowania siatki punktów zainteresowania (POI).
 * <p>
 * UWAGA:
 * Losowe wykluczanie punktów jest zadaniem głównego {@link Generator}'a.
 * Implementacje tego interfejsu zwracają pełną siatkę - bez wykluczeń.
 * <p>
 * Aktualnie dostępne implementacje to:
 * {@link ppi.sensors.benchmark.cli.impl.HoneycombMeshGeneratorImpl},
 * {@link ppi.sensors.benchmark.cli.impl.SquareMeshGeneratorImpl},
 * {@link ppi.sensors.benchmark.cli.impl.TriangleMeshGeneratorImpl}.
 *
 * @see ppi.sensors.benchmark.cli.util.NamedServiceLoader
 * @see ppi.sensors.benchmark.cli.util.ServiceName
 */
public interface PointMeshGenerator {

    /**
     * Zwraca listę punktów tworzących siatkę, zgodnie z zadanymi parametrami.
     * Algorytm generowania siatki zależny jest od klasy implementującej.
     *
     * @param distance   odległość pomiędzy node'ami siatki.
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return lista punktów tworzących siatkę.
     */
    List<Point> createMesh(double distance, int sideLength);
}
