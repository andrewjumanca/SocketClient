import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClient {
    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());
    private static final int DEFAULT_PORT = 1234;

    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.severe("Server address not specified");
            return;
        }

        int port = DEFAULT_PORT;
        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid port number specified: " + args[1], e);
            }
        }

        try (Socket socket = new Socket(args[0], port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            LOGGER.info("Connected to server " + args[0] + ":" + port);

            Thread socketThread = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error reading from socket", e);
                }
            });
            socketThread.start();

            String input;
            while ((input = reader.readLine()) != null && !input.equals("quit")) {
                try (OutputStream out = socket.getOutputStream()) {
                    out.write(input.getBytes());
                    out.flush();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error writing to socket", e);
                }
            }

            socketThread.interrupt();
            socketThread.join();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to server", e);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Threads interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
