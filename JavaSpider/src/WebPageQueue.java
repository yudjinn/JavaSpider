package JavaSpider.src;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Logger;

public class WebPageQueue {
    private static final Logger log = Logger.getLogger(WebPageQueue.class.getName());
    public Queue<Page> pages = new LinkedList<>();
    private List<String> historical = new ArrayList<>();

    public WebPageQueue(String base) {
        pages.add(new Page(base));
        historical.add(base);
    }

    /**
     * Adds page to queue if it hasn't been visited
     * 
     * @param page
     */
    public void addPage(Page page) {
        if (!historical.contains(page.getPath())) {
            pages.add(page);
            historical.add(page.getPath());
            log.info("Page " + page.getPath() + " added to queue.");
        }
    }

    /**
     * get next page in queue
     * 
     * @return
     */
    public Page popPage() {
        if (!pages.isEmpty()) {
            return pages.remove();
        } else {
            log.info("No pages in queue.");
            return null;
        }
    }

    /**
     * Save historical record of pages visited
     */
    public void saveHistorical() {
        try (FileWriter writer = new FileWriter("output/pageList.txt")) {
            for (String line : historical) {
                writer.write(line + System.lineSeparator());
            }
        } catch (IOException e) {
            log.warning("File write error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Return bool if elements exist
     */
    public boolean hasPages() {
        return !pages.isEmpty();
    }

}
