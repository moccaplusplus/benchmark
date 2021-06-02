import ppi.sensors.benchmark.cli.ext.PointMeshGenerator;
import ppi.sensors.benchmark.cli.ext.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.ext.impl.RandomSequenceGeneratorImpl;
import ppi.sensors.benchmark.cli.ext.impl.SquareMeshGeneratorImpl;
import ppi.sensors.benchmark.cli.ext.impl.TriangleMeshGeneratorImpl;

module ppi.sensors.benchmark.cli {

    requires info.picocli;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens ppi.sensors.benchmark.cli;
    opens ppi.sensors.benchmark.cli.model;

    exports ppi.sensors.benchmark.cli;

    uses PointSequenceGenerator;
    uses PointMeshGenerator;

    provides PointSequenceGenerator with RandomSequenceGeneratorImpl;
    provides PointMeshGenerator with SquareMeshGeneratorImpl, TriangleMeshGeneratorImpl;
}