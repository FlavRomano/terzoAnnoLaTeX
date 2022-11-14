// RISULTATI CON FILE TEST DI MOODLE (20 file grandi in media 8KB)
//| Metodo                                                  | Tempo   |
//| ------------------------------------------------------- | ------- |
//| `FileChannel` con buffer indiretto da 16 KB             | 5 ms.   |
//| `FileChannel` con buffer diretto da 16 KB               | 4 ms.   |
//| `FileChannel` con `TransferTo`                          | 3 ms.   |
//| `BufferedStreamIO`                                      | 102 ms. |
//| stream letto in un byte-array gestito dal programmatore | 4 ms.   |

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Nessun file inserito");
            System.err.println("Uso: java Main -b BufferLen file1 file2 ...");
        } else {
            int bufLen = 16 * 1024;
            long time = 0;
            long len = 0;
            for (String filePath : args) {
                FileChannelCopyIndirectBuffer fcIBuf = new FileChannelCopyIndirectBuffer(filePath, bufLen);
                fcIBuf.copy();
                time += fcIBuf.time;
                len += fcIBuf.fileLength;
            }
            System.out.format("FileChannel w/buffer indiretto da %d KB ha impiegato in media %d ms. su %d file grandi mediamente %d KB%n",
                    bufLen/1024, time/args.length, args.length, (len/args.length) / 1024);
            time = 0;
            len = 0;
            for (String filePath : args) {
                FileChannelCopyDirectBuffer fcDBuf = new FileChannelCopyDirectBuffer(filePath, bufLen);
                fcDBuf.copy();
                time += fcDBuf.time;
                len += fcDBuf.fileLength;
            }
            System.out.format("FileChannel w/buffer diretto da %d KB ha impiegato in media %d ms. su %d file grandi mediamente %d KB%n",
                    bufLen/1024, time/args.length, args.length, (len/args.length) / 1024);
            time = 0;
            len = 0;
            for (String filePath : args) {
                FileChannelCopyTransferTo fcTto = new FileChannelCopyTransferTo(filePath);
                fcTto.copy();
                time += fcTto.time;
                len += fcTto.fileLength;
            }
            System.out.format("FileChannel w/TransferTo ha impiegato in media %d ms. su %d file grandi mediamente %d KB%n",
                    time/args.length, args.length, (len/args.length) / 1024);
            time = 0;
            len = 0;
            for (String filePath : args) {
                BufferedStreamCopy bs = new BufferedStreamCopy(filePath);
                bs.copy();
                time += bs.time;
                len += bs.fileLength;
            }
            System.out.format("Buffered Stream IO ha impiegato in media %d ms. su %d file grandi mediamente %d KB%n",
                    time/args.length, args.length, (len/args.length) / 1024);
            time = 0;
            len = 0;
            for (String filePath : args) {
                ByteArrayProgrammatoreCopy bap = new ByteArrayProgrammatoreCopy(filePath, bufLen);
                bap.copy();
                time += bap.time;
                len += bap.fileLength;
            }
            System.out.format("Stream letto in un byte array da %d KB ha impiegato in media %d ms. su %d file grandi mediamente %d KB%n",
                    bufLen/1024, time/args.length, args.length, (len/args.length) / 1024);
        }
    }
}