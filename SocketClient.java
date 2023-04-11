import java.io.*;
import java.net.*;
import java.util.logging.*;

public class SocketClient {
    private static final Logger logger = Logger.getLogger(SocketClient.class.getName());
    private static final String LOG_LEVEL_FLAG = "--loglevel=";

    public static void main(String args[]) {
        String host = args[0];
        int port = Integer.valueOf(args[1]);
        Level logLevel = parseLogLevel(args);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(logLevel);
        logger.addHandler(handler);
        logger.setLevel(logLevel);

        logger.info("Connecting to " + host + ":" + port);
        try (Socket socket = new Socket(host, port)) {
            logger.info("Connected to " + host + ":" + port);
            OutputStream out = socket.getOutputStream();
            String get = "GET / HTTP/1.1";
            out.write(get.getBytes());

            InputStream in = socket.getInputStream();
            int readChar = 0;
            while ((readChar = in.read()) != -1) {
                System.out.write(readChar);
            }
            socket.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Exception occurred", ex);
        }
    }

    private static Level parseLogLevel(String[] args) {
        Level level = Level.INFO;
        for (String arg : args) {
            if (arg.startsWith(LOG_LEVEL_FLAG)) {
                String levelName = arg.substring(LOG_LEVEL_FLAG.length()).toUpperCase();
                level = Level.parse(levelName);
            }
        }
        return level;
    }
}
