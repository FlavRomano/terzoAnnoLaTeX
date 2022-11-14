import java.io.*;

public class BufferedStreamCopy {
    File source;
    long fileLength;
    long time;
    public BufferedStreamCopy(String filePath) {
        this.source = new File(filePath);
        this.fileLength = source.length();
    }
    public void copy() {
        File target = new File(source.getParentFile() + "/outBufStr-" + source.getName());
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
             BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target))) {
            long start = System.currentTimeMillis();
            int byteCounts;
            while ((byteCounts = in.read()) != -1) {
                out.write(byteCounts);
            }
            this.time = System.currentTimeMillis() - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
