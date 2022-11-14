import java.io.*;
import java.nio.channels.FileChannel;

public class FileChannelCopyTransferTo {
    File source;
    long fileLength;
    long time;
    public FileChannelCopyTransferTo(String filePath) {
        this.source = new File(filePath);
        this.fileLength = source.length();
    }
    public void copy() {
        File target = new File(source.getParentFile() + "/outFCTto-" + source.getName());
        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(target).getChannel()) {
            long start = System.currentTimeMillis();
            in.transferTo(0, in.size(), out);
            this.time = System.currentTimeMillis() - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
