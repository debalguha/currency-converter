public class IllegalOperationException extends RuntimeException {
    private final FXEntry sourceEntryToBuiltFrom;

    public IllegalOperationException(String message, FXEntry fxEntry){
        super(message);
        this.sourceEntryToBuiltFrom = fxEntry;
    }

    public FXEntry getSourceEntryToBuiltFrom() {
        return sourceEntryToBuiltFrom;
    }
}
