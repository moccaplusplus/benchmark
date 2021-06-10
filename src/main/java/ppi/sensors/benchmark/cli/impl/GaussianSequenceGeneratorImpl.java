package ppi.sensors.benchmark.cli.impl;

import ppi.sensors.benchmark.cli.util.ServiceName;

/**
 * Pseudolosowy generator sekwencji punktów opartej na rozkładzie Gaussa.
 * Jest to w zasadzie specjalizacja klasy {@link RandomSequenceGeneratorImpl},
 * różniąca się od oryginału tylko tym, że następna wartość pseudolosowa jest
 * generowana według rozkładu Gaussa.
 */
@ServiceName("gaussian")
public class GaussianSequenceGeneratorImpl extends RandomSequenceGeneratorImpl {

    /**
     * Konstruktor z domyślnym ziarnem generatora losowego.
     */
    public GaussianSequenceGeneratorImpl() {
        super();
    }

    /**
     * Konstruktor z możliwością przekazania ziarna dla generatora losowego.
     *
     * @param seed ziarno dla generatora losowego.
     */
    public GaussianSequenceGeneratorImpl(long seed) {
        super(seed);
    }

    /**
     * Zwraca nastepną liczbe pseudolosową, wg rozkładu Gaussa o średniej 1/2 sideLength
     * i odchyleniu standardowym 1/6 * sideLength. Wartości spoza przedziału [0, sideLength]
     * są "przycinane" do granic przedziału.
     *
     * @return następna liczba pseudolosowa wg rozkładu Gaussa.
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     */
    @Override
    protected double nextDouble(int sideLength) {
        double d = random.nextGaussian();
        if (d < -3) d = -3.0;
        else if (d > 3) d = 3.0;
        return sideLength * (d + 3.0) / 6.0 ;
    }
}
