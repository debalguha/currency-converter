import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.stream.Stream;

public class FileDatSync implements DataSync {
    private final String[] headers;
    private final File fileToSyncTo;

    public FileDatSync(File fileToSyncTo, String[] headers) {
        this.headers = headers;
        this.fileToSyncTo = fileToSyncTo;
    }

    @Override
    public void accept(Stream<String> streamOfData) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(fileToSyncTo.getAbsoluteFile(), true))) {
            writer.println(Arrays.stream(headers).reduce((s1, s2) -> s1.concat(",").concat(s2)).get());
            streamOfData.forEach(str -> writer.println(str));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
