import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java SocketClient <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println( "GET / HTTP/1.1");
            out.println("Host: " + host);
            out.println( "Connection: close");
            out.println();

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

