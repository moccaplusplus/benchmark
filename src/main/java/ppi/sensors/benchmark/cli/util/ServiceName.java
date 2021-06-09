package ppi.sensors.benchmark.cli.util;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Anotacja do przypisania nazwy serwisu do klasy serwisu. Ta nazwa jest później używana
 * do filtrowanai dostępnych serwisów przez {@link NamedServiceLoader}.
 *
 * @see NamedServiceLoader
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ServiceName {

    /**
     * Nazwa służąca do identyfikacji serwisu.
     *
     * @return Nazwa do identyfikacji serwisu.
     */
    String value();
}
