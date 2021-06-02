package ppi.sensors.benchmark.cli.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PoiData {

    @JsonProperty
    public String name;

    @JsonProperty
    public long seed;

    @JsonProperty
    public double density;

    @JsonProperty
    public int xRange;

    @JsonProperty
    public int yRange;

    @JsonProperty
    public List<Point> pois;

    @JsonProperty
    public double range;
}
