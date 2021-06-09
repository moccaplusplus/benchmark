import ppi.sensors.benchmark.cli.PointMeshGenerator;
import ppi.sensors.benchmark.cli.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.impl.GaussianSequenceGeneratorImpl;
import ppi.sensors.benchmark.cli.impl.HaltonSequenceGeneratorImpl;
import ppi.sensors.benchmark.cli.impl.HoneycombMeshGeneratorImpl;
import ppi.sensors.benchmark.cli.impl.RandomSequenceGeneratorImpl;
import ppi.sensors.benchmark.cli.impl.SquareMeshGeneratorImpl;
import ppi.sensors.benchmark.cli.impl.TriangleMeshGeneratorImpl;

module ppi.sensors.benchmark.cli {

    requires org.slf4j;
    requires info.picocli;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens ppi.sensors.benchmark.cli;
    opens ppi.sensors.benchmark.cli.model to com.fasterxml.jackson.databind;

    exports ppi.sensors.benchmark.cli;
    exports ppi.sensors.benchmark.cli.model;
    exports ppi.sensors.benchmark.cli.util;
    opens ppi.sensors.benchmark.cli.util;

    uses PointSequenceGenerator;
    uses PointMeshGenerator;

    provides PointSequenceGenerator with RandomSequenceGeneratorImpl, HaltonSequenceGeneratorImpl, GaussianSequenceGeneratorImpl;
    provides PointMeshGenerator with SquareMeshGeneratorImpl, TriangleMeshGeneratorImpl, HoneycombMeshGeneratorImpl;
}