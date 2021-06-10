package ppi.sensors.benchmark.cli;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import ppi.sensors.benchmark.cli.impl.RandomSequenceGeneratorImpl;
import ppi.sensors.benchmark.cli.impl.SquareMeshGeneratorImpl;
import ppi.sensors.benchmark.cli.model.PoiData;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.getServiceName;

public class GeneratorTest {

    @Test
    public void shouldGeneratePoiData() throws IOException {
        // given
        var instanceCount = 10;
        var sideLength = 25;
        var distance = 2.0;
        var density = 0.8;
        var fakeDir = new File(".");
        var mockMapper = mock(JsonMapper.class);
        var mockGenerator = spy(new SquareMeshGeneratorImpl());
        var objectUnderTest = new Generator(mockMapper);

        // when
        objectUnderTest.setInstanceCount(instanceCount);
        objectUnderTest.setSideLength(sideLength);
        objectUnderTest.setPoiDistance(distance);
        objectUnderTest.setPoiDensity(density);
        objectUnderTest.setPointMeshGenerator(mockGenerator);
        objectUnderTest.generatePoiData(fakeDir);

        //then
        verify(mockGenerator, times(1)).createMesh(eq(distance), eq(sideLength));
        var ref = new PoiData();
        ref.xRange = ref.yRange = sideLength;
        ref.range = distance;
        ref.density = density;
        ref.name = getServiceName(SquareMeshGeneratorImpl.class);
        verify(mockMapper, times(instanceCount)).writeValue(any(File.class), refEq(ref, "seed", "pois"));
    }

    @Test
    public void shouldGenerateSensorData() throws IOException {
        // given
        var instanceCount = 10;
        var sideLength = 25;
        var sensorCount = 100;
        var fakeDir = new File(".");
        var mockMapper = mock(JsonMapper.class);
        var mockGenerator = spy(new RandomSequenceGeneratorImpl());
        var objectUnderTest = new Generator(mockMapper);

        // when
        objectUnderTest.setInstanceCount(instanceCount);
        objectUnderTest.setSideLength(sideLength);
        objectUnderTest.setSensorCount(sensorCount);
        objectUnderTest.setPointSequenceGenerator(mockGenerator);
        objectUnderTest.generateSensorData(fakeDir);

        //then
        verify(mockGenerator, times(instanceCount)).createSequence(eq(sensorCount), eq(sideLength));
        verify(mockMapper, times(instanceCount)).writeValue(any(File.class), any());
    }
}