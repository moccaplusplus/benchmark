package ppi.sensors.benchmark.cli;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(sortOptions = false, resourceBundle = "ppi.sensors.benchmark.cli.OptionMessages")
public class Options {

    @Option(names = {"-i", "--instanceCount"}, required = true, paramLabel = "<int>")
    public int instanceCount;

    @Option(names = {"-d", "--poiDistance"}, required = true, paramLabel = "<float>")
    public double poiDistance;

    @Option(names = {"-e", "--poiExclusion"}, required = true, paramLabel = "<int>")
    public int poiExclusionChance;

    @Option(names = {"-m", "--poiMeshType"}, required = true, paramLabel = "<string>")
    public String poiMeshType;

    @Option(names = {"-s", "--sensorCount"}, required = true, paramLabel = "<int>")
    public int sensorCount;

    @Option(names = {"-g", "--generatorType"}, required = true, paramLabel = "<string>")
    public String sensorSequenceType;

    @Option(names = {"-l", "--sideLength"}, required = true, paramLabel = "<int>")
    public int sideLength;

    @Option(names = {"-o", "--outDir"}, paramLabel = "<file>")
    public String outDir = ".";

    @Option(names = {"-h", "--help"}, usageHelp = true)
    public boolean help;
}
