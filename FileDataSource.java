import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileDataSource<T> implements DataSource<T> {

    private final Iterator<String> lineIterator;
    private final String[] headers;
    private final BufferedReader fileBuffered;
    private final BiFunction<String, String[], T> mapper;

    public FileDataSource(File fileToSeedFrom, BiFunction<String, String[], T> mapper) {
        Objects.requireNonNull(fileToSeedFrom, "Can not generate data source with a null file");
        this.mapper = mapper;
        if (!fileToSeedFrom.exists() || !fileToSeedFrom.canRead()) {
            throw new IllegalArgumentException("The file specified is either does not exist or unreadable!!");
        }
        try {
            fileBuffered = new BufferedReader(new FileReader(fileToSeedFrom));
            headers = fileBuffered.readLine().split(",", -1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lineIterator = fileBuffered.lines().iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return new FXEntryIterator();
    }

    @Override
    public Stream<T> getData() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public void close() throws IOException {
        fileBuffered.close();
    }

    class FXEntryIterator implements Iterator<T> {

        @Override
        public boolean hasNext() {
            return lineIterator.hasNext();
        }

        @Override
        public T next() {
            return mapper.apply(lineIterator.next(), headers);
        }
    }
}
