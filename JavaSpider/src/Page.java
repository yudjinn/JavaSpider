package JavaSpider.src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;

public class Page {
    private static final Logger log = Logger.getLogger(Page.class.getName());
    private String path;
    private String data;

    public Page(String path) {
        this.path = path;
    }

    /**
     * Getter for path
     * 
     * @return path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * Getter for data
     * 
     * @return data
     */
    public String getData() {
        return this.data;
    }

    /**
     * Helper for human-readable name
     * 
     * @return index or filename
     */
    public String getName() {
        String name;
        if (this.getPath() != "/") {
            name = this.getPath();
        } else {
            name = "index";
        }
        return name;
    }

    /**
     * Setter for path
     * 
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Setter for data
     * 
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Helper function to save page to local file system based on host path
     */
    public void save() {
        File dir = new File("output");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dir2 = new File("output/sb");
        if (!dir2.exists()) {
            dir2.mkdirs();
        }
        try (FileWriter writer = new FileWriter("output/" + this.getName() + ".html")) {
            for (String line : this.getData().split("\n")) {
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            log.warning("File write error occurred for " + this.getPath());
            e.printStackTrace();
        }
    }

}
