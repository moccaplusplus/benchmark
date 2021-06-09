package ppi.sensors.benchmark.cli;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ppi.sensors.benchmark.cli.model.PoiData;
import ppi.sensors.benchmark.cli.model.Point;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static ppi.sensors.benchmark.cli.util.NamedServiceLoader.getServiceName;

/**
 * Głowna klasa generatora. Odpowiada za generowanie plików benchmarkujących,
 * na podstawie ustawień i przekazanych generatorów siatki {@link PointMeshGenerator}
 * oraz sekwencji punktów {@link PointSequenceGenerator}.
 *
 * @see PointMeshGenerator
 * @see PointSequenceGenerator
 */
public class Generator {

    /**
     * Logger używany do wyświetlania informacji o przebiegu procesu generowania plików.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);

    /**
     * Liczba instancji - czyli liczba wygenerowanych plików.
     * Dotyczy zarówno plików z rozmieszczeniem sensorów jak i plików
     * z rozmieszczeniem punktów POI.
     */
    private int instanceCount;

    /**
     * Długość boku kwadratowego obszaru roboczego, zawierającego punkty zainteresowania POI
     * oraz sensory.
     */
    private int sideLength;

    /**
     * Gęstość POI - czyli prawdopodobieństwo, że punkt siatki nie zostanie wykluczony.
     */
    private double poiDensity;

    /**
     * Odleglość pomiędzy punktami POI. (Przy założeniu, że żaden punkt nie został wykluczony).
     */
    private double poiDistance;

    /**
     * Liczba sensorów do wygenerowania.
     */
    private int sensorCount;

    /**
     * Katalog do zapisu wygenerowanych plików z rozmieszczeniem POI.
     */
    private Path poiOutPath;

    /**
     * Katalog do zapisu wygenerowanych plików z rozmieszczeniem sensorów.
     */
    private Path sensorOutPath;

    /**
     * Generator siatki punktów.
     * Używany do generowania siatki punktów zainteresowania (POI).
     */
    private PointMeshGenerator pointMeshGenerator;

    /**
     * Generator sekwencji punktów.
     * Używany do generowania sekwencji sensorów.
     */
    private PointSequenceGenerator pointSequenceGenerator;

    /**
     * Instancja {@link JsonMapper}'a z biblioteki Jackson.
     * Używana do dumpowania obiektów na format JSON.
     */
    private final JsonMapper jsonMapper;

    /**
     * Domyślny konstruktor.
     * Tworzy obiekt {@link Generator}'a z domyślnym {@link JsonMapper}'em.
     */
    public Generator() {
        this(JsonMapper.builder().build());
    }

    /**
     * Konstruktor z możliwością nadpisania {@link JsonMapper}'a.
     * Pozwala na użycie z mapperem z niestandardowymi ustawieniami.
     *
     * @param jsonMapper instancja {@link JsonMapper}'a.
     */
    public Generator(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    /**
     * Głowna metoda generatora.
     * Rozpoczyna proces generowania plików dla benchmarku.
     * Przed wywołaniem tej metody należy uprzednio skonfigurować obiekt {@link Generator}'a,
     * przy użyciu setterów.
     *
     * @throws IOException w przypadku problemów z zapisem plików lub problemów z utworzeniem
     * katalogow ze ścieżek zapisu.
     */
    public void generate() throws IOException {
        LOGGER.info("Generator started.");
        Files.createDirectories(poiOutPath);
        Files.createDirectories(sensorOutPath);
        generatePoiData(poiOutPath.toFile());
        generateSensorData(sensorOutPath.toFile());
        LOGGER.info("Generator finished.");
    }

    /**
     * Generuje pliki z danymi dla rozmieszczenia POI (Point of Interest).
     *
     * @param outDir katalog w którym zapisane zostaną wygenerowane pliki.
     * @throws IOException w przypadku gdy istnieje problem w zapisie do katalogu.
     */
    void generatePoiData(File outDir) throws IOException {

        final Random random = new Random();

        final PoiData data = new PoiData();
        data.range = poiDistance;
        data.name = getServiceName(pointMeshGenerator);
        data.density = poiDensity;
        data.xRange = data.yRange = sideLength;

        final List<Point> mesh = pointMeshGenerator.createMesh(poiDistance, sideLength);

        LOGGER.info("Generating POI data with params: side length = {}, instance count = {}, distance = {}, density = {}, mesh type = {}",
                sideLength, instanceCount, data.range, data.density, data.name);

        for (int i = 1; i <= instanceCount; i++) {
            final File file = new File(outDir, i + ".json");
            data.seed = Instant.now().toEpochMilli();
            random.setSeed(data.seed);
            data.pois = mesh.stream().filter(p -> random.nextDouble() < poiDensity).collect(toList());
            jsonMapper.writeValue(file, data);
            LOGGER.info("Generated file {}", file);
        }
    }

    /**
     * Generuje pliki z danymi dla rozmieszczenia sensorów.
     *
     * @param outDir katalog w którym zapisane zostaną wygenerowane pliki.
     * @throws IOException w przypadku gdy istnieje problem w zapisie do katalogu.
     */
    void generateSensorData(File outDir) throws IOException {
        LOGGER.info("Generating sensor data with params: side length = {}, instance count = {}, sensor count = {}, generator type = {}",
                sideLength, instanceCount, sensorCount, getServiceName(pointSequenceGenerator));
        for (int i = 1; i <= instanceCount; i++) {
            final File file = new File(outDir, i + ".json");
            final List<Point> data = pointSequenceGenerator.createSequence(sensorCount, sideLength);
            jsonMapper.writeValue(file, data);
            LOGGER.info("Generated file {}", file);
        }
    }

    /**
     * Setter dla pola {@link #instanceCount}.
     *
     * @param instanceCount Liczba instancji - czyli liczba wygenerowanych plików.
     *                      Dotyczy zarówno plików z rozmieszczeniem sensorów jak i plików
     *                      z rozmieszczeniem punktów POI.
     */
    public void setInstanceCount(int instanceCount) {
        this.instanceCount = instanceCount;
    }

    /**
     * Setter dla pola {@link #sideLength}.
     *
     * @param sideLength Długość boku kwadratowego obszaru roboczego,
     *                   zawierającego punkty zainteresowania POI oraz sensory.
     */
    public void setSideLength(int sideLength) {
        this.sideLength = sideLength;
    }

    /**
     * Setter dla pola {@link #poiDensity}.
     *
     * @param poiDensity Gęstość POI - czyli prawdopodobieństwo, że punkt siatki nie zostanie wykluczony.
     */
    public void setPoiDensity(double poiDensity) {
        this.poiDensity = poiDensity;
    }

    /**
     * Setter dla pola {@link #poiDistance}.
     *
     * @param poiDistance Odleglość pomiędzy punktami POI.
     *                    (Przy założeniu, że żaden punkt nie został wykluczony).
     */
    public void setPoiDistance(double poiDistance) {
        this.poiDistance = poiDistance;
    }

    /**
     * Setter dla pola {@link #sensorCount}.
     *
     * @param sensorCount Liczba sensorów do wygenerowania.
     */
    public void setSensorCount(int sensorCount) {
        this.sensorCount = sensorCount;
    }

    /**
     * Setter dla pola {@link #poiOutPath}.
     *
     * @param poiOutPath Katalog do zapisu wygenerowanych plików z rozmieszczeniem POI.
     */
    public void setPoiOutPath(Path poiOutPath) {
        this.poiOutPath = poiOutPath;
    }

    /**
     * Setter dla pola {@link #sensorOutPath}.
     *
     * @param sensorOutPath Katalog do zapisu wygenerowanych plików z rozmieszczeniem sensorów.
     */
    public void setSensorOutPath(Path sensorOutPath) {
        this.sensorOutPath = sensorOutPath;
    }

    /**
     * Setter dla pola {@link #pointMeshGenerator}.
     *
     * @param pointMeshGenerator Generator siatki punktów.
     *                           Używany do generowania siatki punktów zainteresowania (POI).
     */
    public void setPointMeshGenerator(PointMeshGenerator pointMeshGenerator) {
        this.pointMeshGenerator = pointMeshGenerator;
    }

    /**
     * Setter dla pola {@link #pointSequenceGenerator}.
     *
     * @param pointSequenceGenerator Generator sekwencji punktów.
     *                               Używany do generowania sekwencji sensorów.
     */
    public void setPointSequenceGenerator(PointSequenceGenerator pointSequenceGenerator) {
        this.pointSequenceGenerator = pointSequenceGenerator;
    }
}
