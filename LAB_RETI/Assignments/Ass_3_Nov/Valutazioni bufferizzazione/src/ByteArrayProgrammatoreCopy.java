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
            long start = System.currentTimeMillis();
            byte[] byteArray = new byte[bufLen];
            int byteCounts;
            while ((byteCounts = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, byteCounts);
            }
            time = System.currentTimeMillis() - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
