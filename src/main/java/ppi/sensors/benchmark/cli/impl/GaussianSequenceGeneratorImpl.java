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
     * Zwraca nastepną liczbe pseudolosową, wg rozkładu Gaussa o średniej 1/2 sideLength
     * i odchyleniu standardowym 1/2 * sideLength. Wartości spoza przedziału [0, sideLength]
     * są "przycinane" do granic przedziału.
     *
     * @return następna liczba pseudolosowa wg rozkładu Gaussa.
     * @param sideLength długość boku kwadratowego obszaru, w którym znajdują się punkty.
     */
    @Override
    protected double nextDouble(int sideLength) {
        double d = random.nextGaussian();
        if (d < -1) d = -1.0;
        else if (d > 1) d = 1.0;
        return sideLength * (d + 1.0) / 2.0 ;
    }
}
