import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelCopyIndirectBuffer {
    File source;
    long fileLength;
    long time;
    int bufLen;
    public FileChannelCopyIndirectBuffer(String filePath, int bufLen) {
        this.source = new File(filePath);
        this.bufLen = bufLen;
        this.fileLength = source.length();
    }
    public void copy() {
        File target = new File(source.getParentFile() + "/outFCindBuf-" + source.getName());
        try (FileChannel in = new FileInputStream(source).getChannel();
             FileChannel out = new FileOutputStream(target).getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(bufLen);
            long start = System.currentTimeMillis();
            while (in.read(buffer) != -1) {
                buffer.flip();
                out.write(buffer);
                buffer.compact();
            }
            buffer.flip();
            while (buffer.hasRemaining()) {
                out.write(buffer);
            }
            time = System.currentTimeMillis() - start;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
