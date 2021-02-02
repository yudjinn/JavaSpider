package JavaSpider.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class Connection {
    private static final Logger log = Logger.getLogger(Connection.class.getName());
    public String host;
    public Integer port;
    private SSLSocket socket;
    public BufferedReader reader;
    public PrintWriter writer;

    public Connection(String host, Integer port) {
        this.host = host;
        this.port = port;
        initializeSocket(host, port);

    }

    /**
     * Attempt to create SSL socket for communication to host
     * 
     * @param host
     * @param port
     */
    public void initializeSocket(String host, Integer port) {
        try {
            this.socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(host, port);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ioe) {
            log.severe(ioe.getMessage());
        }
    }

}
