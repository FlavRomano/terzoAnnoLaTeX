import java.io.*;

public class ByteArrayProgrammatoreCopy {
    File source;
    long fileLength;
    long time;
    int bufLen;
    public ByteArrayProgrammatoreCopy(String filePath, int bufLen) {
        this.source = new File(filePath);
        this.bufLen = bufLen;
        this.fileLength = source.length();
    }
    public void copy() {
        File target = new File(source.getParentFile() + "/outBArray-" + source.getName());
        try (FileInputStream in = new FileInputStream(source);
             FileOutputStream out = new FileOutputStream(target)) {
            long start = System.nanoTime();
            byte[] buf = new byte[bufLen];
            int byteCounts;
            while ((byteCounts = in.read(buf)) != -1) {
                out.write(buf, 0, byteCounts);
            }
            time = System.nanoTime() - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
