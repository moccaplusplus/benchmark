package ppi.sensors.benchmark.cli;

import com.fasterxml.jackson.databind.json.JsonMapper;
import ppi.sensors.benchmark.cli.ext.PointMeshGenerator;
import ppi.sensors.benchmark.cli.ext.PointSequenceGenerator;
import ppi.sensors.benchmark.cli.model.PoiData;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.getServiceName;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.loadNamedService;

public class Generator {

    private static final String DIR_NAME_POI = "poi";
    private static final String DIR_NAME_SENSOR = "sensor";

    private final int instanceCount;
    private final int sideLength;
    private final double poiDensity;
    private final double poiDistance;
    private final int sensorCount;

    private final Path poiOutPath;
    private final Path sensorOutPath;

    private final PointMeshGenerator pointMeshGenerator;
    private final PointSequenceGenerator pointSequenceGenerator;

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public Generator(Options options) {

        instanceCount = options.instanceCount;
        sideLength = options.sideLength;
        poiDensity = 1.0 - options.poiExclusionChance / 100.0;
        poiDistance = options.poiDistance;
        sensorCount = options.sensorCount;

        pointMeshGenerator = loadNamedService(PointMeshGenerator.class, options.poiMeshType);
        pointSequenceGenerator = loadNamedService(PointSequenceGenerator.class, options.sensorSequenceType);

        final var normalizedOut = Paths.get(options.outDir).toAbsolutePath().normalize().toString();
        final var subDirName = options.poiMeshType + "_" + options.sensorSequenceType + "_" + sideLength;
        poiOutPath = Paths.get(normalizedOut, DIR_NAME_POI, subDirName);
        sensorOutPath = Paths.get(normalizedOut, DIR_NAME_SENSOR, subDirName);
    }

    public void generate() throws IOException {
        Files.createDirectories(poiOutPath);
        Files.createDirectories(sensorOutPath);
        generatePoiData(poiOutPath.toFile());
        generateSensorData(sensorOutPath.toFile());
    }

    private void generatePoiData(File outDir) throws IOException {

        final var random = new Random();

        final var data = new PoiData();
        data.range = poiDistance;
        data.name = getServiceName(pointMeshGenerator);
        data.density = poiDensity;
        data.xRange = data.yRange = sideLength;

        final var mesh = pointMeshGenerator.createMesh(poiDistance, sideLength);

        for (var i = 1; i <= instanceCount; i++) {
            final var file = new File(outDir, i + ".json");
            data.seed = Instant.now().toEpochMilli();
            random.setSeed(data.seed);
            data.pois = mesh.stream().filter(p -> random.nextDouble() < poiDensity).collect(toList());
            jsonMapper.writeValue(file, data);
        }
    }

    private void generateSensorData(File outDir) throws IOException {
        for (var i = 1; i <= instanceCount; i++) {
            final var file = new File(outDir, i + ".json");
            final var data = pointSequenceGenerator.createSequence(sensorCount, sideLength);
            jsonMapper.writeValue(file, data);
        }
    }
}
