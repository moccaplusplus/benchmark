package ppi.sensors.benchmark.cli;

import ppi.sensors.benchmark.cli.model.Point;

import java.util.List;

/**
 * Generator sekwencji punktów.
 * Używany do generowania sekwencji sensorów.
 *
 * Aktualnie dostępne implementacje to:
 * {@link ppi.sensors.benchmark.cli.impl.GaussianSequenceGeneratorImpl},
 * {@link ppi.sensors.benchmark.cli.impl.HaltonSequenceGeneratorImpl},
 * {@link ppi.sensors.benchmark.cli.impl.RandomSequenceGeneratorImpl}.
 *
 * @see ppi.sensors.benchmark.cli.util.NamedServiceLoader
 * @see ppi.sensors.benchmark.cli.util.ServiceName
 */
public interface PointSequenceGenerator {

    /**
     * Zwraca sekwencje punktów zgodnie z zadanymi parametrami.
     * Algorytm generowania punktów zależny jest od klasy implementującej.
     *
     * @param count liczba punktów, którą należy wygenerować (długość sekwencji).
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return sekwencja punktów jako lista.
     */
    List<Point> createSequence(int count, int sideLength);
}
