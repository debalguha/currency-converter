public class ConverterResolutionException extends RuntimeException {
    private final FXEntry fxEntryNotResolved;

    public ConverterResolutionException(FXEntry fxEntry, String message) {
        super(message);
        this.fxEntryNotResolved = fxEntry;
    }

    public FXEntry getFxEntryNotResolved() {
        return fxEntryNotResolved;
    }
}
