package ppi.sensors.benchmark.cli.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    /**
     * Implementacja metody porównania obiektów typu {@link Point}.
     *
     * @param o inny obiekt.
     * @return <code>true</code> jeśli obiekty są takie same,
     * <code>false</code> w przeciwnym razie.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    /**
     * Zwraca hashcode dla danegu punktu.
     *
     * @return hashcode dla danego punktu.
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Zwraca tekstową reprezentację punktu.
     * Używane do debugu.
     *
     * @return tekstowa reprezentacja punktu.
     */
    @Override
    public String toString() {
        return "{" + "x=" + x + ", y=" + y + '}';
    }
}
