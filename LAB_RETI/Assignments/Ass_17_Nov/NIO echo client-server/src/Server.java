import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class Server {
    public static void handleAccept(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketClient = serverSocketChannel.accept();
        socketClient.configureBlocking(false);
        socketClient.register(selector, SelectionKey.OP_READ);
        System.out.format("Connection accepted %s%n", socketClient.getRemoteAddress());
    }

    public static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketClient = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        if (socketClient.read(buffer) == -1) {
            System.out.format("Connection terminated %s%n", socketClient.getRemoteAddress());
            socketClient.close(); // fa anche una key.cancel() implicita
        } else {
            buffer.flip();
            String result = new String(buffer.array()).trim();
            ByteBuffer message = ByteBuffer.wrap(("echoed by server " + result).getBytes());
            socketClient.write(message);
        }
    }

    public static void main(String[] args) throws IOException {
        try (Selector selector = Selector.open();
             ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            ServerSocket socket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8080);
            socket.bind(inetSocketAddress);
            serverSocketChannel.configureBlocking(false);

            int ops = serverSocketChannel.validOps();
            serverSocketChannel.register(selector, ops, null);

            System.out.println("Server listening on port 8080...");
            while (true) {
                selector.select();

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> selectionKeyIt = selectionKeys.iterator();

                while (selectionKeyIt.hasNext()) {
                    SelectionKey key = selectionKeyIt.next();
                    if (key.isAcceptable()) {
                        handleAccept(serverSocketChannel, selector);
                    }
                    if (key.isReadable()) {
                        handleRead(key);
                    }
                    selectionKeyIt.remove();
                }
            }
        }
    }
}