import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopyDirectBuffer {
    File source;
    long fileLength;
    long time;
    int bufLen;
    public FileChannelCopyDirectBuffer(String filePath, int bufLen) {
        this.source = new File(filePath);
        this.bufLen = bufLen;
        this.fileLength = source.length();
    }
    public void copy() {
        File target = new File(source.getParentFile() + "/outFCDirBuf-" + source.getName());
        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(target).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(bufLen);
            long start = System.nanoTime();
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                out.write(buffer);
            }
            time = System.nanoTime() - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
