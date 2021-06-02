package ppi.sensors.benchmark.cli.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Point {

    @JsonProperty
    public double x;

    @JsonProperty
    public double y;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
