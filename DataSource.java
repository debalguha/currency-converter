import java.io.IOException;
import java.util.stream.Stream;

public interface DataSource<T> extends Iterable<T> {
    Stream<T> getData();
    void close() throws IOException;
}
