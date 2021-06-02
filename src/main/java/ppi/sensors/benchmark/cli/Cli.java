package ppi.sensors.benchmark.cli;

import picocli.CommandLine;
import picocli.CommandLine.PicocliException;

import java.io.IOException;

import static ppi.sensors.benchmark.cli.util.ValidationUtil.validate;

public class Cli {

    public static void main(String... args) throws IOException {

        final var options = new Options();
        final var commandLine = new CommandLine(options);

        try {
            commandLine.parseArgs(args);
            if (options.help) {
                commandLine.usage(System.out);
                return;
            }
            validate(options);
        } catch (PicocliException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            commandLine.usage(System.out);
            System.exit(1);
        }

        new Generator(options).generate();
    }
}
