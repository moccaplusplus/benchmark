package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.time.Instant;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;

/**
 * Pseudolosowy generator sekwencji punktów.
 */
@ServiceName("random")
public class RandomSequenceGeneratorImpl implements PointSequenceGenerator {

    /**
     * Generator liczb pseudolosowych z biblioteki standardowej.
     * Używany wewnętrznie.
     */
    protected final Random random;

    /**
     * Konstruktor z domyślnym ziarnem (current timestamp) generatora losowego.
     */
    public RandomSequenceGeneratorImpl() {
        this(Instant.now().toEpochMilli());
    }

    /**
     * Konstruktor z możliwością przekazania ziarna dla generatora losowego.
     *
     * @param seed ziarno dla generatora losowego.
     */
    public RandomSequenceGeneratorImpl(long seed) {
        random = new Random();
        random.setSeed(seed);
    }

    /**
     * Zwraca sekwencję punktów o losowych współrzędnych wewnątrz kwadratowego
     * obszaru zadanego długością boku.
     *
     * @param count      liczba punktów, którą należy wygenerować (długość sekwencji).
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return sekwencja punktów jako lista.
     */
    @Override
    public List<Point> createSequence(int count, int sideLength) {
        return generate(() -> nextPoint(sideLength)).sequential().limit(count).collect(toList());
    }

    /**
     * Zwraca kolejny punkt o losowych współrzędnych wewnątrz kwadratowego
     * obszaru zadanego długością boku.
     *
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return Zwraca kolejny punkt o losowych współrzędnych.
     */
    public Point nextPoint(int sideLength) {
        return new Point(nextDouble(sideLength), nextDouble(sideLength));
    }

    /**
     * Zwraca kolejną liczbę pseudolosową w zakresie [0, sideLength].
     *
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return kolejna liczba pseudolosowa w zakresie [0, sideLength].
     */
    protected double nextDouble(int sideLength) {
        return random.nextDouble() * sideLength;
    }
}
