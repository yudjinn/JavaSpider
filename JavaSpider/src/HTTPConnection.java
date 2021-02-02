package JavaSpider.src;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HTTPConnection extends Connection {
    private static final Logger log = Logger.getLogger(HTTPConnection.class.getName());
    private Map<String, String> headers = new HashMap<>();

    public HTTPConnection(String host, Integer port) {
        super(host, port);
        defaultHeaders(host);
    }

    /**
     * Set default project headers
     */
    public void defaultHeaders(String host) {
        headers.put("Host", host);
    }

    /**
     * Bulk set headers using input map
     * 
     * @param input
     */
    public void setHeaders(Map<String, String> input) {
        for (Map.Entry<String, String> entry : input.entrySet()) {
            this.headers.put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Set header value for given key
     * 
     * @param key
     * @param value
     */
    public void setHeaderByKey(String key, String value) {
        this.headers.put(key, value);
    }

    /**
     * Returns Map of key:value pairs in header
     * 
     * @return
     */
    public Map<String, String> getHeaders() {
        return this.headers;
    }

    /**
     * Returns header value by key
     * 
     * @param key
     * @return
     */
    public String getHeaderByKey(String key) {
        return this.headers.get(key);
    }

    /**
     * Head response to set headers for website
     */
    public void head(String path) {
        this.writer.println("HEAD " + path + " HTTP/1.1");
        sendHeaders(this.writer);
        this.writer.println();
        HTTPResponse response = new HTTPResponse(this.reader);
        setHeaders(response.getHeaders());
    }

    /**
     * Get request for given path
     * 
     * @param path
     * @return HTTPResponse object
     */
    public HTTPResponse get(String path) {
        this.writer.println("GET " + path + " HTTP/1.1");
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            log.info(entry.getKey() + ":" + entry.getValue());
        }
        sendHeaders(this.writer);
        this.writer.println();
        HTTPResponse response = new HTTPResponse(this.reader);
        log.info("GET::" + path + " : " + response.code);
        return response;
    }

    /**
     * Post request for given path using input data map
     * 
     * @param path to request
     * @param data to send
     * 
     * @return HTTPResponse object
     */
    public HTTPResponse post(String path, Map<String, String> data) {
        StringBuilder payload = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (payload.length() == 0) {
                payload.append(entry.getKey() + "=" + entry.getValue());
            } else {
                payload.append("&" + entry.getKey() + "=" + entry.getValue());
            }
        }
        log.info("" + payload.toString().length());

        this.writer.println("POST " + path + " HTTP/1.1");
        setHeaderByKey("Content-Type", "application/x-www-form-urlencoded");
        setHeaderByKey("Content-Length", "" + payload.toString().length());
        sendHeaders(this.writer);
        this.writer.println(payload.toString());
        this.writer.println();
        this.headers.remove("Content-Length");
        this.headers.remove("Content-Type");
        HTTPResponse response = new HTTPResponse(this.reader);
        log.info("POST::" + path + " : " + response.code);
        return response;
    }

    /**
     * Helper function to send headers in request
     */
    public void sendHeaders(PrintWriter writer) {
        for (Map.Entry<String, String> entry : this.headers.entrySet()) {
            writer.println(entry.getKey() + ":" + entry.getValue());
        }
        writer.println();
    }

}
