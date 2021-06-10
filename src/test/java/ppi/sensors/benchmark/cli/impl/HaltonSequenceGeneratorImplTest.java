package ppi.sensors.benchmark.cli.impl;

import org.junit.jupiter.api.Test;
import ppi.sensors.benchmark.cli.model.Point;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HaltonSequenceGeneratorImplTest {

    @Test
    public void shouldCreateSequenceOfGivenSizeWithinGivenArea() {
        // given
        var count = 10000;
        var sideLength = 10000;
        var objectUnderTest = new HaltonSequenceGeneratorImpl();

        // when
        var sequence = objectUnderTest.createSequence(count, sideLength);

        // then
        assertThat(sequence).hasSize(count);
        assertThat(sequence).allMatch(p -> p.x >= 0 && p.x <= sideLength && p.y >= 0 && p.y <= sideLength);
    }

    @Test
    public void shouldCreateSameSequenceInTwoGeneratorInstances() {
        // given
        var count = 10000;
        var sideLength = 10000;
        var objectUnderTest1 = new HaltonSequenceGeneratorImpl();
        var objectUnderTest2 = new HaltonSequenceGeneratorImpl();

        // when
        var sequence1 = objectUnderTest1.createSequence(count, sideLength);
        var sequence2 = objectUnderTest2.createSequence(count, sideLength);

        // then
        assertThat(sequence1).containsExactlyElementsOf(sequence2);
    }

    @Test
    public void shouldCreateDifferentSequencesInOneGenerator() {
        // given
        var count = 10000;
        var sideLength = 10000;
        var objectUnderTest = new HaltonSequenceGeneratorImpl();

        // when
        var sequence1 = objectUnderTest.createSequence(count, sideLength);
        var sequence2 = objectUnderTest.createSequence(count, sideLength);

        // then
        assertThat(sequence1).hasSameSizeAs(sequence2);
        assertThat(sequence1).isNotEqualTo(sequence2);
    }

    @Test
    public void shouldGenerateProperHaltonSequence() {
        // given
        var expected = List.of(
                new Point(0.5, 0.3333333333333333),
                new Point(0.25, 0.6666666666666666),
                new Point(0.75, 0.1111111111111111),
                new Point(0.125, 0.4444444444444444),
                new Point(0.625, 0.7777777777777777),
                new Point(0.375, 0.2222222222222222),
                new Point(0.875, 0.5555555555555556),
                new Point(0.0625, 0.8888888888888888),
                new Point(0.5625, 0.037037037037037035));
        var objectUnderTest = new HaltonSequenceGeneratorImpl();

        // when
        var sequence = objectUnderTest.createSequence(expected.size(), 1);

        // then
        assertThat(sequence).containsExactlyElementsOf(expected);
    }
}