package ppi.sensors.benchmark.cli.impl;

import org.junit.jupiter.api.Test;
import ppi.sensors.benchmark.cli.model.Point;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SquareMeshGeneratorImplTest {

    @Test
    public void shouldCreateMeshWithAllPointsWithinGivenArea() {
        // given
        var distance = 10.0;
        var sideLength = 1000;
        var objectUnderTest = new SquareMeshGeneratorImpl();

        // when
        var mesh = objectUnderTest.createMesh(distance, sideLength);

        // then
        assertThat(mesh).allMatch(p -> p.x >= 0 && p.x <= sideLength && p.y >= 0 && p.y <= sideLength);
    }

    @Test
    public void shouldEveryPointInMeshFormASquare() {
        // given
        var distance = 10.0;
        var sideLength = 100;
        var objectUnderTest = new SquareMeshGeneratorImpl();

        // when
        var mesh = objectUnderTest.createMesh(distance, sideLength);

        // then
        var set = new HashSet<>(mesh);
        assertThat(mesh)
                .filteredOn(p -> p.x + distance <= sideLength && p.y + distance <= sideLength)
                .allMatch(p -> set.containsAll(List.of(
                        new Point(p.x + distance, p.y),
                        new Point(p.x, p.y + distance),
                        new Point(p.x + distance, p.y + distance))));
    }
}