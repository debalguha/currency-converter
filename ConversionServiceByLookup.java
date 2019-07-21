import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;


public class ConversionServiceByLookup implements ConversionService, FXEventListener {
    private final ConcurrentMap<Pair<String, String>, Converter> converterLookupTable;
    private final FXEventHandlerDelegate eventDelegate;
    private boolean supressExceptionToMessage;
    //private final

    public ConversionServiceByLookup(Map<Pair<String, String>, Converter> converterLookupTable) {
        this.converterLookupTable = new ConcurrentHashMap<>();
        this.converterLookupTable.putAll(converterLookupTable);
        this.eventDelegate = new FXEventHandlerDelegate();
        this.supressExceptionToMessage = true;
    }

    public ConversionServiceByLookup(Map<Pair<String, String>, Converter> converterLookupTable, boolean supressExceptionToMessage) {
        this.converterLookupTable = new ConcurrentHashMap<>();
        this.converterLookupTable.putAll(converterLookupTable);
        this.eventDelegate = new FXEventHandlerDelegate();
        this.supressExceptionToMessage = supressExceptionToMessage;
    }

    @Override
    public double doConversion(String fromCurrency, String toCurrency, double valueToConvert) throws NoConverterFoundException, ConverterResolutionException {
        Pair<String, String> currencyPair = Pair.withFromToCurrency(fromCurrency, toCurrency);
        if(converterLookupTable.containsKey(currencyPair)) {
            return createFunctionComposition(converterLookupTable.get(currencyPair), converterLookupTable).apply(valueToConvert);
        } else {
            throw new NoConverterFoundException("Unable to find any converter for given Pair", currencyPair);
        }
    }

    @Override
    public String doConversion(String fromCurrency, String toCurrency, String valueToConvert) {
        if(!Utils.canTransformTODouble(valueToConvert)) {
            return String.format("Unable to parse data[%s] for conversion", valueToConvert);
        }
        try{
            return String.valueOf(doConversion(fromCurrency, toCurrency, Double.valueOf(valueToConvert)));
        } catch(Exception e){
            if(supressExceptionToMessage) {
                return e.getMessage();
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void entryUpdated(FXEvent fxEvent) {
        Optional<Map.Entry<Pair<String, String>, Converter>> optionalEntry = eventDelegate.handleEvent(fxEvent, Collections.unmodifiableMap(converterLookupTable));
        if(optionalEntry.isPresent()){
            converterLookupTable.put(optionalEntry.get().getKey(), optionalEntry.get().getValue());
        }
    }

    private Function<Double, Double> createFunctionComposition(Converter converter, ConcurrentMap<Pair<String, String>, Converter> converterLookupTable) {
        if(converter.getReferencedPairs() != null && !converter.getReferencedPairs().isEmpty()){
            return converter.getReferencedPairs().stream().map(aPair -> createFunctionComposition(converterLookupTable.get(aPair), converterLookupTable)).reduce((func1, func2) -> func1.compose(func2)).get();
        } else if(converter.getConverterFunction().isPresent()){
            return converter.getConverterFunction().get();
        } else {
            throw new RuntimeException("Unable to resolve a converter for "+converter.getPair());
        }
    }
}
