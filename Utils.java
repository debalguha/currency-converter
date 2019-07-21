import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Utils {
    protected static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");


    public static boolean isEmptystring(String anyString){
        return anyString == null || anyString.isEmpty();
    }
    public static Pair<String, String>[] toRefPairs(FXEntry fxEntry){
        Pair<String, String>[] referencedPairs = new Pair[]{new Pair<>(fxEntry.getFromCurrency(), fxEntry.getConversionEntry()),
                new Pair<>(fxEntry.getConversionEntry(), fxEntry.getToCurrency())};
        return referencedPairs;
    }
    public static boolean existCyclicReference(Pair<String, String> firstPair, Pair<String, String> secondPair, Set<Pair<String, String>> visitedEntries) {
        return visitedEntries.contains(firstPair) || visitedEntries.contains(secondPair);
    }

    public static boolean canTransformTODouble(String conversionEntry) {
        return DOUBLE_PATTERN.matcher(conversionEntry).matches();
    }

    public static Function<Double, Double> buildFunction(final Double factor) {
        return input -> factor * input;
    }

    public static BiFunction<String, String[], Collection<FXEntry>> lineToFXEntriesMapping(){
        return (line, headers) -> {
            if(isEmptystring(line)) {
                return Collections.emptyList();
            }
            String[] elements = line.split(",", -1);
            String fromCurrency = elements[0].trim();
            return IntStream.range(1, elements.length).mapToObj((index) -> new FXEntry(fromCurrency, headers[index].trim(), elements[index].trim())).collect(Collectors.toList());
        };
    }

    public static BiFunction<String, String[], Map<String, String>> lineToMapMapping(){
        return (line, headers) -> {
            if(isEmptystring(line)) {
                return Collections.emptyMap();
            }
            String[] elements = line.split(",", -1);
            return IntStream.range(0, elements.length).<Map.Entry<String, String>>mapToObj((index) -> new HashMap.SimpleEntry<>(headers[index].trim(), elements[index].trim()))
                    .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
        };
    }
}
