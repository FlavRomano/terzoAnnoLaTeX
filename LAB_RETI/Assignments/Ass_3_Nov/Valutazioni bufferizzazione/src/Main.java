// RISULTATI CON FILE TEST DI MOODLE (20 file grandi in media 8KB)
//| Metodo                                                  |  Tempo medio  |
//| ------------------------------------------------------- | ------------- |
//| `FileChannel` con buffer indiretto da 16 KB             |    5 ms.      |
//| `FileChannel` con buffer diretto da 16 KB               |    4 ms.      |
//| `FileChannel` con `TransferTo`                          |    3 ms.      |
//| `BufferedStreamIO`                                      |    102 ms.    |
//| stream letto in un byte-array gestito dal programmatore |    4 ms.      |

public class Main {
    public static void confronto(String filePath, int bufLen) {
        System.out.println("================================================");
        FileChannelCopyIndirectBuffer fcIBuf = new FileChannelCopyIndirectBuffer(filePath, bufLen);
        System.out.format("Dimensione file: %.2f KB%n", (float)fcIBuf.fileLength / 1024);
        fcIBuf.copy();
        System.out.format("%10d ns. - FileChannel w/buffer indiretto%n", fcIBuf.time);

        FileChannelCopyDirectBuffer fcDBuf = new FileChannelCopyDirectBuffer(filePath, bufLen);
        fcDBuf.copy();
        System.out.format("%10d ns. - FileChannel w/buffer diretto%n", fcDBuf.time);

        FileChannelCopyTransferTo fcTto = new FileChannelCopyTransferTo(filePath);
        fcTto.copy();
        System.out.format("%10d ns. - FileChannel w/TransferTo%n", fcTto.time);

        BufferedStreamCopy bs = new BufferedStreamCopy(filePath);
        bs.copy();
        System.out.format("%10d ns. - Buffered Stream IO %n", bs.time);

        ByteArrayProgrammatoreCopy bap = new ByteArrayProgrammatoreCopy(filePath, bufLen);
        bap.copy();
        System.out.format("%10d ns. - Stream letto in un byte array%n", bap.time);
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Nessun file inserito");
            System.err.println("Uso: java Main file1 file2 ...");
        } else {
            int bufLen = 16 * 1024;
            System.out.format("Dimensione buffer: %d KB%n", bufLen);
            for (String filepath : args) {
                confronto(filepath, bufLen);
            }
            System.out.println("================================================");
        }
    }
}