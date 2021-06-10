package ppi.sensors.benchmark.cli.impl;

import org.junit.jupiter.api.Test;
import ppi.sensors.benchmark.cli.model.Point;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HoneycombMeshGeneratorImplTest {

    @Test
    public void shouldCreateMeshWithAllPointsWithinGivenArea() {
        // given
        var distance = 10.0;
        var sideLength = 1000;
        var objectUnderTest = new HoneycombMeshGeneratorImpl();

        // when
        var mesh = objectUnderTest.createMesh(distance, sideLength);

        // then
        assertThat(mesh).allMatch(p -> p.x >= 0 && p.x <= sideLength && p.y >= 0 && p.y <= sideLength);
    }

    @Test
    public void shouldEveryPointInMeshFormAHoneycomb() {
        // given
        var distance = 10.0;
        var sideLength = 100;
        var objectUnderTest = new HoneycombMeshGeneratorImpl();

        // when
        var mesh = objectUnderTest.createMesh(distance, sideLength);

        // then
        var set = new HashSet<>(mesh);
        var height = distance * Math.sqrt(3) / 2;
        assertThat(mesh)
                .filteredOn(p -> p.x + 2 * distance <= sideLength && p.y + height <= sideLength && p.y - height >= 0)
                .filteredOn(p -> !set.contains(new Point(p.x + distance, p.y)))
                .allMatch(p -> set.containsAll(List.of(
                        new Point(p.x + 2 * distance, p.y),
                        new Point(p.x + 0.5 * distance, p.y - height),
                        new Point(p.x + 0.5 * distance, p.y + height),
                        new Point(p.x + 1.5 * distance, p.y - height),
                        new Point(p.x + 1.5 * distance, p.y + height)
                )));
    }
}