package ppi.sensors.benchmark.cli.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Reprezentuje model danych opisujących wygenerowaną siatkę punktów zainteresowania (POI).
 * Używany do serializacji pojedynczego pliku z wygenerowaną siatką.
 */
public class PoiData {

    /**
     * Nazwa generatora siatki.
     */
    @JsonProperty
    public String name;

    /**
     * Wartość ziarna przkazana do generatora losowego.
     */
    @JsonProperty
    public long seed;

    /**
     *
     */
    @JsonProperty
    public double density;

    /**
     * Szerokość obszaru na którym generowane są punkty zainteresowania (POI).
     */
    @JsonProperty
    public int xRange;

    /**
     * Wysokość obszaru na którym generowane są punkty zainteresowania (POI).
     */
    @JsonProperty
    public int yRange;

    /**
     * Lista punktów zainterosowania (POI).
     */
    @JsonProperty
    public List<Point> pois;

    /**
     * Oczekiwana odległość między punktami zainteresowania POI
     * (przed wykluczeniem losowych POI z siatki).
     */
    @JsonProperty
    public double range;
}
