package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.model.Point;
import ppi.sensors.benchmark.cli.util.ServiceName;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.generate;

/**
 * Generator ciągu punktów wg. ciągu Haltona.
 * Współrzędna x n-tego punktu zawiera n-tą liczbę ciągu Haltona dla bazy 2.
 * Współrzędna y n-tego punktu zawiera n-tą liczbę ciągu Haltona dla bazy 3.
 *
 * Implementacja oparta na pseudokodzie z https://en.wikipedia.org/wiki/Halton_sequence.
 */
@ServiceName("halton")
public class HaltonSequenceGeneratorImpl implements PointSequenceGenerator {

    /**
     * Licznik - wykorzystywany wewnętrznie.
     */
    private int counter = 0;

    /**
     * Zwraca sekwencje punktów. Gdzie dla n-tego punktu współrzędna x
     * to n-ty wyraz ciągu Haltona o bazie 2, zaś współrzędna y to n-ty wyraz ciągu
     * Haltona o bazie 3.
     *
     * @param count liczba punktów, którą należy wygenerować (długość sekwencji).
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return sekwencja punktów jako lista.
     */
    @Override
    public List<Point> createSequence(int count, int sideLength) {
        return generate(() -> nextPoint(sideLength)).sequential().limit(count).collect(toList());
    }

    /**
     * Zwraca kolejny punkt (wg licznika {@link #counter}).
     * Gdzie współrzędna x to kolejny wyraz ciągu Haltona o bazie 2,
     * zaś współrzędna y to kolejny wyraz ciągu Haltona o bazie 3.
     *
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     * @return Zwraca kolejny punkt (wg licznika {@link #counter})
     */
    public Point nextPoint(int sideLength) {
        final int index = ++counter;
        return new Point(
                haltonNumber(index, 2) * sideLength,
                haltonNumber(index, 3) * sideLength);
    }

    /**
     * Zwraca kolejny wyraz ciągu Haltona dla podanego indeksu i bazy.
     * Implementacja oparta na pseudokodzie z https://en.wikipedia.org/wiki/Halton_sequence.
     *
     * @param index - index wyrazu ciągu Haltona.
     * @param base  - baza (oczekiwana liczba pierwsza).
     * @return kolejny wyraz ciągu Haltona dla podanego indeksu i bazy.
     */
    private double haltonNumber(int index, int base) {
        double result = 0.0;
        double factor = 1.0;

        while (index > 0) {
            factor /= base;
            result += factor * (index % base);
            index /= base;
        }

        return result;
    }
}
