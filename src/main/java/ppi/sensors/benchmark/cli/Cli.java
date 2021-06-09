package ppi.sensors.benchmark.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ParseResult;
import ppi.sensors.benchmark.cli.util.ValidationException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import static java.lang.System.err;
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
@Command(sortOptions = false, resourceBundle = "ppi.sensors.benchmark.cli.OptionMessages")
public class Cli implements Callable<Integer> {

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
    int instanceCount;

    /**
     * Pole do którego wczytywana jest wartość parametru "-d", "--poiDistance".
     * Znaczenie:
     * Odległość między węzłami siatki.
     */
    @Option(names = {"-d", "--poiDistance"}, required = true, paramLabel = "<float>")
    double poiDistance;

    /**
     * Pole do którego wczytywana jest wartość parametru "-e", "--poiExclusion".
     * Znaczenie:
     * Prawdopodobieństwo wykluczenia POI w procentach.
     * (Aby uniknąć regularności w rozkładzie POI, część losowo wybranych węzłówsiatki nie zawiera POI).
     */
    @Option(names = {"-e", "--poiExclusion"}, required = true, paramLabel = "<int>")
    int poiExclusionChance;

    /**
     * Pole do którego wczytywana jest wartość parametru "-m", "--poiMeshType".
     * Znaczenie:
     * Rodzaj siatki zawierającej punkty zainteresowania.
     */
    @Option(names = {"-m", "--poiMeshType"}, required = true, paramLabel = "<string>")
    String poiMeshType;

    /**
     * Pole do którego wczytywana jest wartość parametru "-s", "--sensorCount".
     * Znaczenie:
     * Liczba sensorów.
     */
    @Option(names = {"-s", "--sensorCount"}, required = true, paramLabel = "<int>")
    int sensorCount;

    /**
     * Pole do którego wczytywana jest wartość parametru "-g", "--generatorType".
     * Znaczenie:
     * Rodzaj generatora dystrybucji sensorów.
     */
    @Option(names = {"-g", "--generatorType"}, required = true, paramLabel = "<string>")
    String sensorSequenceType;

    /**
     * Pole do którego wczytywana jest wartość parametru "-l", "--sideLength".
     * Znaczenie:
     * Długość boku kwadratowego obszaru zawierającego punkty zainteresowania i sensory.
     */
    @Option(names = {"-l", "--sideLength"}, required = true, paramLabel = "<int>")
    int sideLength;

    /**
     * Pole do którego wczytywana jest wartość parametru "-o", "--outDir".
     * Znaczenie:
     * Folder gdzie zapisać wyniki - domyślnie current working directory.
     */
    @Option(names = {"-o", "--outDir"}, paramLabel = "<file>")
    String outDir = "./target";

    /**
     * Help command - obsługiwane wewnętrznie przez bibliotekę Picocli.
     */
    @Option(names = {"-h", "--help"}, usageHelp = true)
    boolean help;

    /**
     * Entry point do programu.
     * Parsuje argumenty przy użyciu biblioteki Picocli, nastepnie tworzy instancje {@link Cli}
     * i wywoluje na niej metodę {@link #call()} (robi to obiekt {@link CommandLine} z biblioteki
     * Picocli).
     *
     * @param args argumenty z linii poleceń.
     */
    public static void main(String... args) {
        System.exit(new CommandLine(new Cli())
                .setParameterExceptionHandler(Cli::parameterExceptionHandler)
                .setExecutionExceptionHandler(Cli::executionExceptionHandler)
                .execute(args));
    }

    /**
     * Exception handler dla exceptionow wyrzuconych w trakcie parsowania argumentów
     * z linii poleceń przez bibliotekę Picocli.
     *
     * @param e exception wyrzucony w trakcie parsowania argumentów.
     * @param args argumenty przekazane do parsowania.
     * @return kod zakończenia programu.
     */
    private static int parameterExceptionHandler(ParameterException e, String[] args) {
        err.println(e.getMessage());
        e.getCommandLine().usage(out);
        return 1;
    }

    /**
     * Exception handler dla exceptionow wyrzuconych w trakcie wykonywania programu
     * przez bibliotekę Picocli. (tj. w trakcie wywolania metody {@link Callable#call()}
     * z obiektu przekazanego do kontruktora {@link CommandLine}.
     *
     * @param e nieobslużony exception wyrzucany w trakcie wykonania programu.
     * @param commandLine aktualny obiekt {@link CommandLine}.
     * @param parseResult wynik parsowania.
     * @return kod zakończenia programu.
     */
    private static int executionExceptionHandler(Exception e, CommandLine commandLine, ParseResult parseResult) {
        if (e instanceof ValidationException) {
            err.println(e.getMessage());
            commandLine.usage(out);
        } else {
            e.printStackTrace(err);
        }
        return 1;
    }

    private static String msg(String key, Object... params) {
        return format(MESSAGES.getString(key), params);
    }

    /**
     * Główny "punkt wejścia" do programu commandline'owego.
     * Wyłowywany automatycznie przez obiekt {@link CommandLine} po sparsowaniu przekazanych
     * argumentów do pól obiektu {@link Cli}.
     * Odpala {@link Generator} po uprzedniej walidacji pól i setupie generatora zgodnie z
     * argumentami commandline.
     *
     * @return kod zakończenia programu.
     * @throws IOException w przypadku problemów z zapisem plików lub utworzeniem katalogu.
     * @throws ValidationException w przypadku gdy argumenty commandline nie spełniły kryteriów
     * walidacyjnych.
     */
    @Override
    public Integer call() throws IOException, ValidationException {
        validate();
        generate();
        return 0;
    }

    /**
     * Tworzy obiekt {@link Generator}'a, konfiguruje go zgodnie z wartościami pól odczytanych
     * z argumentów commandline, a następnie startuje proces generowania plików (poprzez
     * odpalenie metedy {@link Generator#generate()}).
     *
     * @throws IOException w przypadku problemów z zapisem plików lub utworzeniem katalogu.
     */
    private void generate() throws IOException {
        final Generator generator = new Generator();

        generator.setInstanceCount(instanceCount);
        generator.setSideLength(sideLength);
        generator.setPoiDensity(1.0 - poiExclusionChance / 100.0);
        generator.setPoiDistance(poiDistance);
        generator.setSensorCount(sensorCount);
        generator.setPointMeshGenerator(loadNamedService(PointMeshGenerator.class, poiMeshType));
        generator.setPointSequenceGenerator(loadNamedService(PointSequenceGenerator.class, sensorSequenceType));

        final String normalizedOut = Paths.get(outDir).toAbsolutePath().normalize().toString();
        final String subDirName = (poiMeshType + "_" + sensorSequenceType + "_" + sideLength).toLowerCase();
        generator.setPoiOutPath(Paths.get(normalizedOut, DIR_NAME_POI, subDirName));
        generator.setSensorOutPath(Paths.get(normalizedOut, DIR_NAME_SENSOR, subDirName));

        generator.generate();
    }

    /**
     * Sprawdza wartości pól obiekt {@link Cli} pod kątem zgodności z kryteriami walidacyjnymi.
     *
     * @throws ValidationException gdy któreś z pól nie spełnia kryteriów walidacyjnych.
     */
    private void validate() throws ValidationException {
        final List<String> errors = new ArrayList<>();

        if (instanceCount < MIN_INSTANCE_COUNT || instanceCount > MAX_INSTANCE_COUNT)
            errors.add(msg("error.instanceCount", instanceCount,
                    MIN_INSTANCE_COUNT, MAX_INSTANCE_COUNT));

        if (sideLength < MIN_SIDE_LENGTH || sideLength > MAX_SIDE_LENGTH)
            errors.add(msg("error.sideLength", sideLength, MIN_SIDE_LENGTH, MAX_SIDE_LENGTH));

        final double minPoiDistance = MIN_POI_DISTANCE_TO_SIDE_LENGTH * sideLength;
        final double maxPoiDistance = MAX_POI_DISTANCE_TO_SIDE_LENGTH * sideLength;
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

        final Path outPath = Paths.get(outDir);
        if (!Files.exists(outPath) || !Files.isDirectory(outPath))
            errors.add(msg("error.outDir", outDir));

        if (errors.size() > 0) throw new ValidationException(String.join("\n", errors));
    }
}
