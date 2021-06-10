package ppi.sensors.benchmark.cli.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ppi.sensors.benchmark.cli.PointMeshGenerator;
import ppi.sensors.benchmark.cli.model.Point;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.ThrowableAssert.catchThrowable;

public class NamedServiceLoaderTest {

    public static class NoopPointMeshGenerator implements PointMeshGenerator {
        @Override
        public List<Point> createMesh(double distance, int sideLength) {
            return null;
        }
    }

    @ServiceName("unregistered")
    public static class NamedNoopPointMeshGenerator extends NoopPointMeshGenerator {
    }

    public static class RegisteredNoopPointMeshGenerator extends NoopPointMeshGenerator {
    }

    @ServiceName("registered")
    public static class NamedRegisteredNoopPointMeshGenerator extends NoopPointMeshGenerator {
    }

    @Test
    public void shouldFindAllServiceNames() {
        // given
        var serviceClass = PointMeshGenerator.class;

        // when
        var names = NamedServiceLoader.getNamesForType(serviceClass);

        // then
        Assertions.assertThat(names).hasSameElementsAs(List.of("square", "triangle", "honeycomb", "registered"));
    }

    @Test
    public void shouldGetNameOfUnregisteredWithAnnotation() {
        // given
        var serviceInstance = new NamedNoopPointMeshGenerator();

        // when
        var name = NamedServiceLoader.getServiceName(serviceInstance);

        // then
        Assertions.assertThat(name).isEqualTo("unregistered");
    }

    @Test
    public void shouldNotKnowAboutUnregisteredAnnotatedService() {
        // given
        var serviceClass = PointMeshGenerator.class;
        var requestedName = "unregistered";

        // when
        var result = NamedServiceLoader.hasNamedService(serviceClass, requestedName);

        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void shouldThrowWhenTryingToLoadInvalidServiceName() {
        // given
        var serviceClass = PointMeshGenerator.class;
        var requestedName = "halton";

        // when
        var thrown = catchThrowable(() -> NamedServiceLoader.loadNamedService(serviceClass, requestedName));

        // then
        Assertions.assertThat(thrown).isInstanceOf(NoSuchElementException.class);
    }
}