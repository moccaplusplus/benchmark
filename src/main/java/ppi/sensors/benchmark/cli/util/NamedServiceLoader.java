package ppi.sensors.benchmark.cli.util;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

public class NamedServiceLoader {

    public static <T> List<String> getNamesForType(Class<T> baseClass) {
        return ServiceLoader.load(baseClass).stream()
                .map(ServiceLoader.Provider::type)
                .map(NamedServiceLoader::getServiceName)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    public static <T> boolean hasNamedService(Class<T> baseClass, String requestedName) {
        return ServiceLoader.load(baseClass).stream()
                .anyMatch(s -> hasServiceName(s.type(), requestedName));
    }

    public static <T> T loadNamedService(Class<T> baseClass, String requestedName) {
        return ServiceLoader.load(baseClass)
                .stream()
                .filter(s -> hasServiceName(s.type(), requestedName))
                .map(ServiceLoader.Provider::get)
                .findAny()
                .orElseThrow();
    }

    public static <T> String getServiceName(Class<? extends T> actualClass) {
        return Optional.of(actualClass)
                .map(c -> c.getAnnotation(ServiceName.class))
                .map(ServiceName::value)
                .orElse(null);
    }

    public static <T> String getServiceName(T instance) {
        return getServiceName(instance.getClass());
    }

    public static <T> boolean hasServiceName(Class<? extends T> actualClass, String requestedName) {
        return Optional.of(actualClass)
                .map(c -> c.getAnnotation(ServiceName.class))
                .map(ServiceName::value)
                .map(requestedName::equalsIgnoreCase)
                .orElse(false);
    }
}
