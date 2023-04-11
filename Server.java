// import java.io.*;
// import java.net.*;

// public class Server {
//     public static void main(String[] args) {
//         int port = 1234;
        
//         try (ServerSocket serverSocket = new ServerSocket(port)) {
//             System.out.println("Listening on port " + port + "...");
            
//             while (true) {
//                 Socket clientSocket = serverSocket.accept();
//                 System.out.println("Accepted connection from " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
                
//                 try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
//                     String inputLine;
//                     while ((inputLine = in.readLine()) != null) {
//                         System.out.println("Received message: " + inputLine);
//                         out.println("Server says: " + inputLine);
//                     }
//                 } catch (IOException e) {
//                     System.err.println("Error handling client connection: " + e.getMessage());
//                 }
//             }
//         } catch (IOException e) {
//             System.err.println("Error starting server: " + e.getMessage());
//         }
//     }
// }

import java.io.*;
import java.net.*;
import java.util.logging.*;

public class Server {
    private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Server <port> <client>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String client = args[1];

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            LOGGER.log(Level.INFO, "Server listening on port {0}", port);

            Socket socket = serverSocket.accept();
            LOGGER.log(Level.INFO, "Client connected from {0}", socket.getInetAddress());

            Thread ioThread = new Thread(() -> {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                     PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

                    String line;
                    while ((line = in.readLine()) != null) {
                        LOGGER.log(Level.INFO, "Sending input: {0}", line);
                        out.println(line);
                    }

                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Error sending input to client", ex);
                }
            });
            ioThread.start();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    LOGGER.log(Level.INFO, "Received output: {0}", line);
                    System.out.println(line);
                }
            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error running server", ex);
        }
    }
}
