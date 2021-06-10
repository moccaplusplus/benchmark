package ppi.sensors.benchmark.cli;

import org.junit.jupiter.api.Test;
import ppi.sensors.benchmark.cli.util.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;
import static ppi.sensors.benchmark.cli.Cli.MAX_INSTANCE_COUNT;
import static ppi.sensors.benchmark.cli.Cli.MAX_POI_DISTANCE_TO_SIDE_LENGTH;
import static ppi.sensors.benchmark.cli.Cli.MAX_SENSOR_COUNT;
import static ppi.sensors.benchmark.cli.Cli.MIN_INSTANCE_COUNT;
import static ppi.sensors.benchmark.cli.Cli.MIN_POI_DISTANCE_TO_SIDE_LENGTH;
import static ppi.sensors.benchmark.cli.Cli.MIN_SENSOR_COUNT;
import static ppi.sensors.benchmark.cli.Cli.msg;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.getNamesForType;

public class CliTest {

    @Test
    public void shouldPassValidation() {
        // given
        var objectUnderTest = new Cli();
        objectUnderTest.instanceCount = 10;
        objectUnderTest.sideLength = 25;
        objectUnderTest.poiDistance = 1.2;
        objectUnderTest.poiExclusionChance = 20;
        objectUnderTest.poiMeshType = "triangle";
        objectUnderTest.sensorCount = 200;
        objectUnderTest.sensorSequenceType = "halton";
        objectUnderTest.outDir = ".";

        // when
        ValidationException thrown = catchThrowableOfType(objectUnderTest::validate, ValidationException.class);

        // then
        assertThat(thrown).doesNotThrowAnyException();
    }

    @Test
    public void shouldNotPassValidationDueToInvalidInstanceCountAndSensorCount() {
        // given
        var objectUnderTest = new Cli();
        objectUnderTest.instanceCount = -10;
        objectUnderTest.sideLength = 25;
        objectUnderTest.poiDistance = 1.2;
        objectUnderTest.poiExclusionChance = 20;
        objectUnderTest.poiMeshType = "triangle";
        objectUnderTest.sensorCount = 200000;
        objectUnderTest.sensorSequenceType = "halton";
        objectUnderTest.outDir = ".";

        // when
        ValidationException thrown = catchThrowableOfType(objectUnderTest::validate, ValidationException.class);

        // then
        assertThat(thrown).hasMessageContainingAll(
                msg("error.instanceCount", objectUnderTest.instanceCount, MIN_INSTANCE_COUNT, MAX_INSTANCE_COUNT),
                msg("error.sensorCount", objectUnderTest.sensorCount, MIN_SENSOR_COUNT, MAX_SENSOR_COUNT)
        );
    }

    @Test
    public void shouldNotPassValidationDueToInvalidGeneratorAndNullMesh() {
        // given
        var objectUnderTest = new Cli();
        objectUnderTest.instanceCount = 10;
        objectUnderTest.sideLength = 25;
        objectUnderTest.poiDistance = 1.2;
        objectUnderTest.poiExclusionChance = 20;
        objectUnderTest.sensorCount = 200;
        objectUnderTest.sensorSequenceType = "invalid";
        objectUnderTest.outDir = ".";

        // when
        ValidationException thrown = catchThrowableOfType(objectUnderTest::validate, ValidationException.class);

        // then
        assertThat(thrown).hasMessageContainingAll(
                msg("error.poiMeshType", null, getNamesForType(PointMeshGenerator.class)),
                msg("error.sensorSequenceType", objectUnderTest.sensorSequenceType, getNamesForType(PointSequenceGenerator.class))
        );
    }

    @Test
    public void shouldNotPassValidationDueToNonExistingOutDir() {
        // given
        var objectUnderTest = new Cli();
        objectUnderTest.instanceCount = 10;
        objectUnderTest.sideLength = 25;
        objectUnderTest.poiDistance = 1.2;
        objectUnderTest.poiExclusionChance = 20;
        objectUnderTest.poiMeshType = "triangle";
        objectUnderTest.sensorCount = 200;
        objectUnderTest.sensorSequenceType = "halton";
        objectUnderTest.outDir = "/some/non/existing/path/f513113/44842552/43262462/2526";

        // when
        ValidationException thrown = catchThrowableOfType(objectUnderTest::validate, ValidationException.class);

        // then
        assertThat(thrown).hasMessage(msg("error.outDir", objectUnderTest.outDir));
    }

    @Test
    public void shouldNotPassValidationDueToInvalidPoiDistanceInRelationToSideLength() {
        // given
        var objectUnderTest = new Cli();
        objectUnderTest.instanceCount = 10;
        objectUnderTest.sideLength = 25;
        objectUnderTest.poiDistance = 5;
        objectUnderTest.poiExclusionChance = 20;
        objectUnderTest.poiMeshType = "triangle";
        objectUnderTest.sensorCount = 200;
        objectUnderTest.sensorSequenceType = "halton";

        // when
        ValidationException thrown = catchThrowableOfType(objectUnderTest::validate, ValidationException.class);

        // then
        assertThat(thrown).hasMessage(msg("error.poiDistance", objectUnderTest.poiDistance,
                objectUnderTest.sideLength * MIN_POI_DISTANCE_TO_SIDE_LENGTH,
                objectUnderTest.sideLength * MAX_POI_DISTANCE_TO_SIDE_LENGTH,
                objectUnderTest.sideLength,
                MIN_POI_DISTANCE_TO_SIDE_LENGTH, MAX_POI_DISTANCE_TO_SIDE_LENGTH));
    }
}