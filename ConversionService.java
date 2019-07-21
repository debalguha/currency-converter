public interface ConversionService {
    double doConversion(String fromCurrency, String toCurrency, double valueToConvert);
    String doConversion(String fromCurrency, String toCurrency, String valueToConvert);
}
