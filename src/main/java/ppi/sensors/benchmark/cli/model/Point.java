package ppi.sensors.benchmark.cli.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Reprezentuje punkt na płaszczyźnie.
 * Używany do serializacji położenia punktów zaiteresowania (POI) oraz sensorów.
 */
public class Point {

    /**
     * Współrzędna x punktu na płaszczyźnie.
     */
    @JsonProperty
    public double x;

    /**
     * Współrzędna y punktu na płaszczyźnie.
     */
    @JsonProperty
    public double y;

    /**
     * Domyślny konstruktor. Tworzy punkt o współrzędnych (0, 0).
     */
    public Point() {
        this(0, 0);
    }

    /**
     * Konstruktor. Tworzy punkt o współrzędnych (x, y).
     *
     * @param x współrzędna x punktu na płaszczyźnie.
     * @param y współrzędna y punktu na płaszczyźnie.
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
