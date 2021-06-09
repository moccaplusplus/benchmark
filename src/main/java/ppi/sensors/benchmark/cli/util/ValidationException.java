package ppi.sensors.benchmark.cli.util;

/**
 * Błąd walidacji.
 * Klasa potrzebna do wyłapywania wyłącznie wyjątków walidacyjnych,
 * dlatego nie mogłem skorzystać z generycznego typo exceptiona.
 *
 * @see ppi.sensors.benchmark.cli.Cli
 */
public class ValidationException extends Exception {

    /**
     * Konstruktor z parametrem "message" zawierającym informacje o błędzie walidacji.
     *
     * @param message zawiera informacje o zaistniałych błędach walidacji.
     */
    public ValidationException(String message) {
        super(message);
    }
}
