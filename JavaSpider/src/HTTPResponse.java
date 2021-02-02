package JavaSpider.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HTTPResponse {
    private static final Logger log = Logger.getLogger(HTTPResponse.class.getName());
    public String body;
    public Integer code;
    public String raw;

    public HTTPResponse(BufferedReader input) {
        try {
            this.raw = getRaw(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.code = getCode(raw);
        this.body = getBody(raw);
    }

    /**
     * Helper function to parse headers from raw response
     * 
     * @return Map of headers
     */
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        for (String line : raw.split("\n")) {
            if (line.isEmpty()) {
                break;
            }
            if (line.startsWith("HTTP") || line.startsWith("Date")) {
                continue;
            }
            String key = line.split(":")[0];
            if (key.equals("Set-Cookie")) {
                if (headers.containsKey("Cookie")) {
                    headers.put("Cookie", headers.get("Cookie") + ";" + line.split(":")[1].split(";")[0]);
                } else {
                    headers.put("Cookie", line.split(":")[1].split(";")[0]);
                }
            }
        }
        return headers;
    }

    /**
     * Helper function to parse body from response string
     * 
     * @param input
     * @return body
     */
    public String getBody(String input) {
        return input.substring(input.indexOf("\n\n") + 2);
    }

    /**
     * Helper function to parse response code from response string
     * 
     * @param raw
     * @return
     */
    public Integer getCode(String raw) {
        return Integer.parseInt(raw.split("\n")[0].split(" ")[1]);
    }

    /**
     * Helper function to build raw String from buffered reader
     * 
     * @param input
     * @return raw string of response
     * @throws IOException
     */
    public String getRaw(BufferedReader input) throws IOException {
        StringBuilder sb = new StringBuilder();
        String in;
        while ((in = input.readLine()) != null && input.ready()) {
            sb.append(in + System.lineSeparator());
        }
        return sb.toString();
    }

}
