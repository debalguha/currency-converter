import java.util.Objects;

public class Pair<T, V> {
    private final T t;
    private final V v;
    public Pair(T t, V v){
        this.t = t;
        this.v = v;
    }
    public T _1(){
        return t;
    }
    public V _2(){
        return v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(t, pair.t) &&
                Objects.equals(v, pair.v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, v);
    }

    public static Pair<String, String> fromFXEntry(FXEntry fxEntry){
        return new Pair<>(fxEntry.getFromCurrency(), fxEntry.getToCurrency());
    }

    public static Pair<String, String> withFromToCurrency(String fromCurrency, String toCurrency) {
        return new Pair<>(fromCurrency, toCurrency);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "t=" + t +
                ", v=" + v +
                '}';
    }
}
