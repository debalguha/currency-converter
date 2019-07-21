import java.util.Objects;

public class FXEntry {
    public final String fromCurrency;
    private final String toCurrency;
    private final String conversionEntry;

    public FXEntry(String fromCurrency, String toCurrency, String conversionEntry) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.conversionEntry = conversionEntry;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public String getConversionEntry() {
        return conversionEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FXEntry fxEntry = (FXEntry) o;
        return Objects.equals(fromCurrency, fxEntry.fromCurrency) &&
                Objects.equals(toCurrency, fxEntry.toCurrency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromCurrency, toCurrency);
    }

    @Override
    public String toString() {
        return "FXEntry{" +
                "fromCurrency='" + fromCurrency + '\'' +
                ", toCurrency='" + toCurrency + '\'' +
                ", conversionEntry='" + conversionEntry + '\'' +
                '}';
    }
}
