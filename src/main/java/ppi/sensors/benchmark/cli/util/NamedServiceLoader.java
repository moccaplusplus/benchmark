package ppi.sensors.benchmark.cli.util;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

/**
 * Utility do pracy z serwisami filtrowanymi po arbitralnej nazwie (nadanej
 * przy użyciu anotacji {@link ServiceName}).
 *
 * @see ServiceName
 */
public class NamedServiceLoader {

    /**
     * Zwraca listę nazw wszystkich zarejestrowanych implementacji danego interfejsu.
     *
     * @param baseClass klasa interfejsu serwisu.
     * @return lista nazw wszystkich zarejestrowanych implementacji danego interfejsu.
     */
    public static List<String> getNamesForType(Class<?> baseClass) {
        return ServiceLoader.load(baseClass, NamedServiceLoader.class.getClassLoader()).stream()
                .map(ServiceLoader.Provider::type)
                .map(NamedServiceLoader::getServiceName)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * Sprawdza czy jest zarejstrowany serwis o podanej nazwie, implementujący podany interfejs.
     *
     * @param baseClass klasa interfejsu serwisu.
     * @param requestedName sprawdzana nazwa.
     * @return <code>true</code> jeśli jest zarejstrowany serwis o podanej nazwie,
     * implementujący podany interfejs, <code>false</code> w przeciwnym przypadku.
     */
    public static boolean hasNamedService(Class<?> baseClass, String requestedName) {
        return ServiceLoader.load(baseClass, NamedServiceLoader.class.getClassLoader()).stream()
                .anyMatch(s -> hasServiceName(s.type(), requestedName));
    }

    /**
     * Zwraca instancje serwisu o podanej nazwie, implementujący podany interfejs.
     *
     * @param baseClass klasa interfejsu serwisu.
     * @param requestedName nazwa serwisu.
     * @param <T> typ interfejsu serwisu.
     * @return instancja serwisu o podanej nazwie, implementujący podany interfejs.
     * @throws NoSuchElementException jeśli nie istnieje serwis o podanej nazwie,
     * implementujący podany interfejs.
     */
    public static <T> T loadNamedService(Class<T> baseClass, String requestedName) {
        return ServiceLoader.load(baseClass, NamedServiceLoader.class.getClassLoader())
                .stream()
                .filter(s -> hasServiceName(s.type(), requestedName))
                .map(ServiceLoader.Provider::get)
                .findAny()
                .orElseThrow();
    }

    /**
     * Zwraca nazwę serwisu (nadaną anotacją {@link ServiceName}) dla podanej klasy serwisu.
     * Jest to w zasadzie util na pobranie nazwy z anotacji przypiętej do klasy serwisu.
     *
     * @param actualClass konkretna klasa serwisu (nie interfejsu bazowego).
     * @return nazwa serwisu lub <code>null</code> jeśli dany serwis nie ma nazwy.
     */
    public static String getServiceName(Class<?> actualClass) {
        return Optional.of(actualClass)
                .map(c -> c.getAnnotation(ServiceName.class))
                .map(ServiceName::value)
                .orElse(null);
    }

    /**
     * Zwraca nazwę serwisu (nadaną anotacją {@link ServiceName}) dla podanej instancji serwisu.
     * Jest to w zasadzie util na pobranie nazwy z anotacji przypiętej do klasy serwisu.
     *
     * @param instance instancja serwisu.
     * @return nazwa serwisu lub <code>null</code> jeśli dany serwis nie ma nazwy.
     */
    public static String getServiceName(Object instance) {
        return getServiceName(instance.getClass());
    }

    /**
     * Sprawdza czy dana klasa serwisu jest oznaczona podaną nazwą.
     *
     * @param actualClass konkretna klasa serwisu.
     * @param requestedName sprawdzana nazwa.
     * @return <code>true</code> jeśli dana klasa serwisu jest oznaczona podaną nazwą.
     * <code>false</code> w przeciwnym wypadku.
     */
    public static boolean hasServiceName(Class<?> actualClass, String requestedName) {
        return Optional.of(actualClass)
                .map(c -> c.getAnnotation(ServiceName.class))
                .map(ServiceName::value)
                .map(requestedName::equalsIgnoreCase)
                .orElse(false);
    }

    /**
     * Prywatny konstruktor żeby uniemożliwić tworzenie instancji util'a,
     * który ma wyłącznie statyczne metody.
     */
    private NamedServiceLoader() {}
}
