import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.function.Function;

public class ConverterImpl implements Converter {
    private final Pair<String, String> fxPair;
    private final Optional<String> unresolvedFactorOrValue;
    private final Optional<Function<Double, Double>> converterFunction;

    private final Queue<Pair<String, String>> referencedPairs = new LinkedList<>();

    public ConverterImpl(Pair<String, String> fxPair, Function<Double, Double> converterFunction) {
        this.fxPair = fxPair;
        this.converterFunction = Optional.of(converterFunction);
        this.unresolvedFactorOrValue = Optional.empty();
    }

    public ConverterImpl(Pair<String, String> fxPair, Pair<String, String> firstReference, Pair<String, String> secondReferene) {
        this.fxPair = fxPair;
        referencedPairs.offer(firstReference);
        referencedPairs.offer(secondReferene);
        this.converterFunction = Optional.of(Function.identity());
        this.unresolvedFactorOrValue = Optional.empty();
    }

    public ConverterImpl(FXEntry fxEntry) {
        this.fxPair = Pair.fromFXEntry(fxEntry);
        this.converterFunction = Optional.empty();
        this.unresolvedFactorOrValue = Optional.of(fxEntry.getConversionEntry());
    }

    public ConverterImpl(Pair<String, String> fxPair) {
        this.fxPair = fxPair;
        this.converterFunction = Optional.empty();
        this.unresolvedFactorOrValue = Optional.empty();
    }

    @Override
    public Pair<String, String> getPair() {
        return fxPair;
    }

    @Override
    public Optional<Function<Double, Double>> getConverterFunction() {
        return this.converterFunction;
    }

    @Override
    public Optional<String> getUnresolvedFactorOrValue() {
        return unresolvedFactorOrValue;
    }

    @Override
    public Queue<Pair<String, String>> getReferencedPairs() {
        return referencedPairs;
    }
}
