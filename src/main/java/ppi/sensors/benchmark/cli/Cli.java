package ppi.sensors.benchmark.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import ppi.sensors.benchmark.cli.util.ValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.System.err;
import static java.lang.System.exit;
import static java.lang.System.out;
import static java.text.MessageFormat.format;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.getNamesForType;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.hasNamedService;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.loadNamedService;

/**
 * Główna klasa programu (z metodą main, ustawiona jak main-class w pliku MANIFEST.MF).
 * Parsuje parametry z linii poleceń, a następnie je waliduje.
 * Jeśli są poprawne to tworzy objekt {@link Generator}'a.
 * Odpowiednio go konfiguruje i odpala metodę {@link Generator#generate()}.
 */
@Command(
        name = "java -jar benchmark-cli.jar",
        sortOptions = false,
        resourceBundle = "ppi.sensors.benchmark.cli.OptionMessages")
public class Cli {

    /**
     * Nazwa subkatalogu, w którym zostaną zapisane wygenerowane pliki z rozmieszczeniem POI.
     */
    public static final String DIR_NAME_POI = "poi";

    /**
     * Nazwa subkatalogu, w którym zostaną zapisane wygenerowane pliki z rozmieszczeniem sensorów.
     */
    public static final String DIR_NAME_SENSOR = "sensor";

    /**
     * Wartość maksymalna dla {@link #instanceCount}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MAX_INSTANCE_COUNT = 100;

    /**
     * Wartość minimalna dla {@link #instanceCount}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MIN_INSTANCE_COUNT = 1;

    /**
     * Wartość maksymalna dla {@link #sideLength}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MAX_SIDE_LENGTH = 1000;

    /**
     * Wartość minmalna dla {@link #sideLength}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MIN_SIDE_LENGTH = 1;

    /**
     * Wartość maksymalna dla {@link #poiDistance} w relacji do wartości {@link #sideLength}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final double MAX_POI_DISTANCE_TO_SIDE_LENGTH = 0.1;

    /**
     * Wartość minmalna dla {@link #poiDistance} w relacji do wartości {@link #sideLength}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final double MIN_POI_DISTANCE_TO_SIDE_LENGTH = 0.001;

    /**
     * Wartość maksymalna dla {@link #sensorCount}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MAX_SENSOR_COUNT = 10000;

    /**
     * Wartość minmalna dla {@link #sensorCount}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MIN_SENSOR_COUNT = 1;

    /**
     * Wartość maksymalna dla {@link #poiExclusionChance}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MAX_POI_EXCLUSION_CHANCE = 100;

    /**
     * Wartość minmalna dla {@link #poiExclusionChance}.
     * Używane podczas walidacji w {@link #validate()}.
     */
    public static final int MIN_POI_EXCLUSION_CHANCE = 0;

    /**
     * Zestaw message'y dla komunikatów błędu.
     */
    public static final ResourceBundle MESSAGES = ResourceBundle.getBundle("ppi.sensors.benchmark.cli.ErrorMessages");

    /**
     * Pole do którego wczytywana jest wartość parametru  "-i", "--instanceCount".
     * Znaczenie:
     * Liczba instancji.
     */
    @Option(names = {"-i", "--instanceCount"}, required = true, paramLabel = "<int>")
    /* visible for tests */ int instanceCount;

    /**
     * Pole do którego wczytywana jest wartość parametru "-d", "--poiDistance".
     * Znaczenie:
     * Odległość między węzłami siatki.
     */
    @Option(names = {"-d", "--poiDistance"}, required = true, paramLabel = "<float>")
    /* visible for tests */ double poiDistance;

    /**
     * Pole do którego wczytywana jest wartość parametru "-e", "--poiExclusion".
     * Znaczenie:
     * Prawdopodobieństwo wykluczenia POI w procentach.
     * (Aby uniknąć regularności w rozkładzie POI, część losowo wybranych węzłówsiatki nie zawiera POI).
     */
    @Option(names = {"-e", "--poiExclusion"}, required = true, paramLabel = "<int>")
    /* visible for tests */ int poiExclusionChance;

    /**
     * Pole do którego wczytywana jest wartość parametru "-m", "--poiMeshType".
     * Znaczenie:
     * Rodzaj siatki zawierającej punkty zainteresowania.
     */
    @Option(names = {"-m", "--poiMeshType"}, required = true, paramLabel = "<string>")
    /* visible for tests */ String poiMeshType;

    /**
     * Pole do którego wczytywana jest wartość parametru "-s", "--sensorCount".
     * Znaczenie:
     * Liczba sensorów.
     */
    @Option(names = {"-s", "--sensorCount"}, required = true, paramLabel = "<int>")
    /* visible for tests */ int sensorCount;

    /**
     * Pole do którego wczytywana jest wartość parametru "-g", "--generatorType".
     * Znaczenie:
     * Rodzaj generatora dystrybucji sensorów.
     */
    @Option(names = {"-g", "--generatorType"}, required = true, paramLabel = "<string>")
    /* visible for tests */ String sensorSequenceType;

    /**
     * Pole do którego wczytywana jest wartość parametru "-l", "--sideLength".
     * Znaczenie:
     * Długość boku kwadratowego obszaru zawierającego punkty zainteresowania i sensory.
     */
    @Option(names = {"-l", "--sideLength"}, required = true, paramLabel = "<int>")
    /* visible for tests */ int sideLength;

    /**
     * Pole do którego wczytywana jest wartość parametru "-o", "--outDir".
     * Znaczenie:
     * Folder gdzie zapisać wyniki - domyślnie current working directory.
     */
    @Option(names = {"-o", "--outDir"}, paramLabel = "<file>")
    /* visible for tests */ String outDir = ".";

    /**
     * Help command - opcja wyświetlenia pomocy.
     */
    @Option(names = {"-h", "--help"}, usageHelp = true)
    /* visible for tests */ boolean help;

    /**
     * Entry point do programu.
     * Parsuje argumenty przy użyciu biblioteki Picocli, a następnie tworzy instancje {@link Cli}
     * i wywoluje na niej metodę {@link #run()}.
     *
     * @param args argumenty z linii poleceń.
     * @throws IOException w przypadku problemów z zapisem plików lub utworzeniem katalogu.
     */
    public static void main(String... args) throws IOException {

        final var cli = new Cli();
        final var commandLine = new CommandLine(cli);

        try {
            commandLine.parseArgs(args);
        } catch (ParameterException e) {
            err.println(e.getClass().getName() + ": " + e.getMessage());
            e.getCommandLine().usage(out);
            exit(1);
        }

        if (cli.help) {
            commandLine.usage(out);
            exit(0);
        }

        try {
            cli.run();
        } catch (ValidationException e) {
            err.println(e.getMessage());
            commandLine.usage(out);
        }
    }

    /**
     * Używana wewnętrznie.
     * Utility do wygodnego wyciągania message'y z resource bundle'a oraz formatowania z parametrami.
     *
     * @param key    klucz message'a.
     * @param params parametry do podmiany (zakładając,że dany message wymaga parametrów).
     * @return sformatowany i zlokalizowany tekst message'a.
     */
    /* visible for tests */
    static String msg(String key, Object... params) {
        return format(MESSAGES.getString(key), params);
    }

    /**
     * Wyłowywany w {@link #main(String...)} po sparsowaniu przekazanych argumentów do pól obiektu {@link Cli}.
     * Odpala {@link Generator} po uprzedniej walidacji pól i setupie generatora zgodnie z argumentami commandline.
     *
     * @throws IOException         w przypadku problemów z zapisem plików lub utworzeniem katalogu.
     * @throws ValidationException w przypadku gdy argumenty commandline nie spełniły kryteriów walidacyjnych.
     */
    public void run() throws IOException, ValidationException {
        validate();
        generate();
    }

    /**
     * Tworzy obiekt {@link Generator}'a, konfiguruje go zgodnie z wartościami pól odczytanych
     * z argumentów commandline, a następnie startuje proces generowania plików (poprzez
     * odpalenie metedy {@link Generator#generate()}).
     *
     * @throws IOException w przypadku problemów z zapisem plików lub utworzeniem katalogu.
     */
    /* visible for tests */ void generate() throws IOException {
        final var generator = new Generator();

        generator.setInstanceCount(instanceCount);
        generator.setSideLength(sideLength);
        generator.setPoiDensity(1.0 - poiExclusionChance / 100.0);
        generator.setPoiDistance(poiDistance);
        generator.setSensorCount(sensorCount);
        generator.setPointMeshGenerator(loadNamedService(PointMeshGenerator.class, poiMeshType));
        generator.setPointSequenceGenerator(loadNamedService(PointSequenceGenerator.class, sensorSequenceType));

        final var normalizedOut = Paths.get(outDir).toAbsolutePath().normalize().toString();
        final var subDirName = (poiMeshType + "_" + sensorSequenceType + "_" + sideLength).toLowerCase();
        generator.setPoiOutPath(Paths.get(normalizedOut, DIR_NAME_POI, subDirName));
        generator.setSensorOutPath(Paths.get(normalizedOut, DIR_NAME_SENSOR, subDirName));

        generator.generate();
    }

    /**
     * Sprawdza wartości pól obiektu {@link Cli} pod kątem zgodności z kryteriami walidacyjnymi.
     *
     * @throws ValidationException gdy któreś z pól nie spełnia kryteriów walidacyjnych.
     */
    /* visible for tests */ void validate() throws ValidationException {
        final var errors = new ArrayList<String>();

        if (instanceCount < MIN_INSTANCE_COUNT || instanceCount > MAX_INSTANCE_COUNT)
            errors.add(msg("error.instanceCount", instanceCount,
                    MIN_INSTANCE_COUNT, MAX_INSTANCE_COUNT));

        if (sideLength < MIN_SIDE_LENGTH || sideLength > MAX_SIDE_LENGTH)
            errors.add(msg("error.sideLength", sideLength, MIN_SIDE_LENGTH, MAX_SIDE_LENGTH));

        final var minPoiDistance = MIN_POI_DISTANCE_TO_SIDE_LENGTH * sideLength;
        final var maxPoiDistance = MAX_POI_DISTANCE_TO_SIDE_LENGTH * sideLength;
        if (poiDistance < minPoiDistance || poiDistance > maxPoiDistance)
            errors.add(msg("error.poiDistance", poiDistance, minPoiDistance, maxPoiDistance,
                    sideLength, MIN_POI_DISTANCE_TO_SIDE_LENGTH, MAX_POI_DISTANCE_TO_SIDE_LENGTH));

        if (poiExclusionChance < MIN_POI_EXCLUSION_CHANCE || poiExclusionChance > MAX_POI_EXCLUSION_CHANCE)
            errors.add(msg("error.poiExclusionChance", poiExclusionChance,
                    MIN_POI_EXCLUSION_CHANCE, MAX_POI_EXCLUSION_CHANCE));

        if (poiMeshType == null || !hasNamedService(PointMeshGenerator.class, poiMeshType))
            errors.add(msg("error.poiMeshType", poiMeshType, getNamesForType(PointMeshGenerator.class)));

        if (sensorCount < MIN_SENSOR_COUNT || sensorCount > MAX_SENSOR_COUNT)
            errors.add(msg("error.sensorCount", sensorCount, MIN_SENSOR_COUNT, MAX_SENSOR_COUNT));

        if (sensorSequenceType == null || !hasNamedService(PointSequenceGenerator.class, sensorSequenceType))
            errors.add(msg("error.sensorSequenceType", sensorSequenceType, getNamesForType(PointSequenceGenerator.class)));

        final var outPath = Paths.get(outDir);
        if (!Files.exists(outPath) || !Files.isDirectory(outPath))
            errors.add(msg("error.outDir", outDir));

        if (errors.size() > 0) throw new ValidationException(String.join("\n", errors));
    }
}
