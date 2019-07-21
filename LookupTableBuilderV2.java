import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class LookupTableBuilderV2 {
    protected DataSource<Collection<FXEntry>> refDataSource;
    public static LookupTableBuilderV2 withRefDataSource(DataSource<Collection<FXEntry>> refDataSource) {
        LookupTableBuilderV2 lookupTableBuilder = new LookupTableBuilderV2();
        lookupTableBuilder.refDataSource = refDataSource;
        return lookupTableBuilder;
    }

    public Map<Pair<String, String>, Converter> buildFXConversionTable() throws IOException {
        Map<Pair<String, String>, Converter> conversionTable = new HashMap<>();
        try {
            Map<Pair<String, String>, FXEntry> pairFXEntryMap = refDataSource.getData().flatMap(Collection::stream).collect(Collectors.toMap(fxEntry -> Pair.fromFXEntry(fxEntry), Function.identity()));
            for(Pair<String, String> currentPair : pairFXEntryMap.keySet()) {
                FXEntry currentEntry = pairFXEntryMap.get(currentPair);
                conversionTable.putIfAbsent(currentPair, processEntry(currentEntry, currentPair, pairFXEntryMap, conversionTable, new HashSet<>()));
            }
        } finally {
            refDataSource.close();
        }
        return conversionTable;
    }

    private Converter processEntry(FXEntry currentEntry, Pair<String, String> currentPair, Map<Pair<String, String>, FXEntry> pairFXEntryMap, Map<Pair<String, String>, Converter> conversionTable, Set<Pair<String, String>> visitedEntries) {
        //System.out.println("Processing:: "+currentEntry + " and current pair:: "+currentPair);
        visitedEntries.add(currentPair);
        if(currentEntry == null){
            return new ConverterImpl(currentPair);
        }
        if(Utils.isEmptystring(currentEntry.getConversionEntry())){
            return new ConverterImpl(currentEntry);
        }
        if(Utils.canTransformTODouble(currentEntry.getConversionEntry())){
            return new ConverterImpl(currentPair, Utils.buildFunction(Double.parseDouble(currentEntry.getConversionEntry())));
        } else {
            if(currentPair._1().equals(currentPair._2())){
                return new ConverterImpl(currentEntry);
            }
            final Pair<String, String>[] referencedPair = Utils.toRefPairs(currentEntry);
            if(Utils.existCyclicReference(referencedPair[0], referencedPair[1], visitedEntries)){
                //System.out.println(logForCyclicReference(referencedPair[0], referencedPair[1], visitedEntries));
                return new ConverterImpl(currentEntry);
            }
            Optional<Converter> firstRefConverter = lookupConverterFromConversionTable(referencedPair[0], conversionTable);
            if(!firstRefConverter.isPresent()){
                firstRefConverter = Optional.of(processEntry(pairFXEntryMap.get(referencedPair[0]), referencedPair[0], pairFXEntryMap, conversionTable, visitedEntries));
                conversionTable.put(referencedPair[0], firstRefConverter.get());
            }

            Optional<Converter> secondRefConverter = lookupConverterFromConversionTable(referencedPair[1], conversionTable);
            if(!secondRefConverter.isPresent()){
                secondRefConverter = Optional.of(processEntry(pairFXEntryMap.get(referencedPair[1]), referencedPair[1], pairFXEntryMap, conversionTable, visitedEntries));
                conversionTable.put(referencedPair[1], secondRefConverter.get());
            }

            if(firstRefConverter.isPresent() && firstRefConverter.get().getConverterFunction().isPresent()
                && secondRefConverter.isPresent() && secondRefConverter.get().getConverterFunction().isPresent()){
                return new ConverterImpl(currentPair, firstRefConverter.get().getPair(), secondRefConverter.get().getPair());
            }
            return new ConverterImpl(currentEntry);
        }
    }

    private String logForCyclicReference(Pair<String, String> referencePair1, Pair<String, String> referencePair2, Set<Pair<String, String>> visitedEntries) {
        StringBuilder builder = new StringBuilder("Detected cyclic reference ");
        builder.append(String.format("First Pair [%s]", referencePair1)).append(String.format("Second Pair [%s]", referencePair2));
        builder.append(String.format("\n\t Visited Entries [%s]", visitedEntries));
        return builder.toString();
    }

    private Optional<Converter> lookupConverterFromConversionTable(Pair<String, String> pair, Map<Pair<String, String>, Converter> conversionTable) {
        return Optional.ofNullable(conversionTable.get(pair));
    }

}
