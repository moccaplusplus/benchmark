package ppi.sensors.benchmark.cli.util;

import ppi.sensors.benchmark.cli.Options;
import ppi.sensors.benchmark.cli.ext.PointMeshGenerator;
import ppi.sensors.benchmark.cli.ext.PointSequenceGenerator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.text.MessageFormat.format;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.hasNamedService;

public class ValidationUtil {

    public static final int MAX_INSTANCE_COUNT = 100;
    public static final int MAX_SIDE_LENGTH = 1000;
    public static final double MAX_POI_DISTANCE_TO_SIDE_LENGTH = 0.1;
    public static final int MAX_SENSOR_COUNT = 10000;
    public static final int MAX_POI_EXCLUSION_CHANCE = 100;

    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle("ppi.sensors.benchmark.cli.ErrorMessages");

    public static void validate(Options options) throws IllegalArgumentException {
        final var errors = new ArrayList<String>();

        if (options.instanceCount <= 0 || options.instanceCount > MAX_INSTANCE_COUNT)
            errors.add(msg("error.instanceCount", options.instanceCount));
        if (options.sideLength <= 0 || options.sideLength > MAX_SIDE_LENGTH)
            errors.add(msg("error.sideLength", options.sideLength));
        if (options.poiDistance <= 0 || options.poiDistance > MAX_POI_DISTANCE_TO_SIDE_LENGTH * options.sideLength)
            errors.add(msg("error.poiDistance", options.poiDistance));
        if (options.poiExclusionChance < 0 || options.poiExclusionChance > MAX_POI_EXCLUSION_CHANCE)
            errors.add(msg("error.poiExclusionChance", options.poiExclusionChance));
        if (options.poiMeshType == null || !hasNamedService(PointMeshGenerator.class, options.poiMeshType))
            errors.add(msg("error.poiMeshType", options.poiMeshType));
        if (options.sensorCount <= 0 || options.sensorCount > MAX_SENSOR_COUNT)
            errors.add(msg("error.sensorCount", options.sensorCount));
        if (options.sensorSequenceType == null || !hasNamedService(PointSequenceGenerator.class, options.sensorSequenceType))
            errors.add(msg("error.sensorSequenceType", options.sensorSequenceType));

        final var outPath = Paths.get(options.outDir);
        if (!Files.exists(outPath) || !Files.isDirectory(outPath))
            errors.add(msg("error.outDir", options.outDir));

        if (errors.size() > 0) throw new IllegalArgumentException(String.join("\n", errors));
    }

    private static String msg(String key, Object... params) {
        return format(MESSAGES.getString(key), params);
    }
}
