public class NoConverterFoundException extends RuntimeException {
    private Pair<String, String> currencyPair;

    public NoConverterFoundException(String message, Pair<String, String> currencyPair) {
        super(message);
        this.currencyPair = currencyPair;
    }

    public Pair<String, String> getCurrencyPair() {
        return currencyPair;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getMessage()).append(String.format(" Given Pair[%s]", currencyPair));
        return builder.toString();
    }
}
