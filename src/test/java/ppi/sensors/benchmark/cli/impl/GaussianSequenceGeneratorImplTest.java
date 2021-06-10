package ppi.sensors.benchmark.cli.impl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GaussianSequenceGeneratorImplTest {

    @Test
    public void shouldCreateSequenceOfGivenSizeWithinGivenArea() {
        // given
        var count = 10000;
        var sideLength = 10000;
        var objectUnderTest = new GaussianSequenceGeneratorImpl();

        // when
        var sequence = objectUnderTest.createSequence(count, sideLength);

        // then
        assertThat(sequence).hasSize(count);
        assertThat(sequence).allMatch(p -> p.x >= 0 && p.x <= sideLength && p.y >= 0 && p.y <= sideLength);
    }

    @Test
    public void shouldCreateSameSequenceInTwoGeneratorsWithSameSeed() {
        // given
        var count = 10000;
        var sideLength = 10000;
        var objectUnderTest1 = new GaussianSequenceGeneratorImpl(6871162586L);
        var objectUnderTest2 = new GaussianSequenceGeneratorImpl(6871162586L);

        // when
        var sequence1 = objectUnderTest1.createSequence(count, sideLength);
        var sequence2 = objectUnderTest2.createSequence(count, sideLength);

        // then
        assertThat(sequence1).hasSameElementsAs(sequence2);
    }

    @Test
    public void shouldCreateDifferentSequencesInTwoGeneratorsWithDifferentSeed() {
        // given
        var count = 10000;
        var sideLength = 10000;
        var objectUnderTest1 = new GaussianSequenceGeneratorImpl(9156871100L);
        var objectUnderTest2 = new GaussianSequenceGeneratorImpl(2002262586L);

        // when
        var sequence1 = objectUnderTest1.createSequence(count, sideLength);
        var sequence2 = objectUnderTest2.createSequence(count, sideLength);

        // then
        assertThat(sequence1).hasSameSizeAs(sequence2);
        assertThat(sequence1).isNotEqualTo(sequence2);
    }
}