import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;

public interface Converter {
    Pair<String, String> getPair();
    Optional<Function<Double, Double>> getConverterFunction();
    Optional<String> getUnresolvedFactorOrValue();

    Queue<Pair<String, String>> getReferencedPairs();
}
