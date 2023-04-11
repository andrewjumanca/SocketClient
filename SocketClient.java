import java.io.*;
import java.net.*;
import java.util.logging.*;

public class SocketClient {
    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java SocketClient <server> <port> [input ...]");
            System.exit(1);
        }

        String server = args[0];
        int port = Integer.parseInt(args[1]);

        String[] input = new String[0];
        if (args.length > 2) {
            input[0] = args[2];
        }

        try (Socket socket = new Socket(server, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            LOGGER.log(Level.INFO, "Connected to {0}:{1}", new Object[] {server, port});

            for (String s : input) {
                LOGGER.log(Level.INFO, "Sending input: {0}", s);
                out.println(s);
            }

            String line;
            while ((line = in.readLine()) != null) {
                LOGGER.log(Level.INFO, "Received output: {0}", line);
                System.out.println(line);
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error connecting to server", ex);
        }
    }
}
