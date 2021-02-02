package JavaSpider.src;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebPageParser {
    private static final Logger log = Logger.getLogger(WebPageParser.class.getName());

    /**
     * Jsoup parser to get links from html string
     * 
     * @param pageData
     * @return List of pages
     */
    public List<Page> getLinks(String pageData) {
        List<Page> output = new ArrayList<Page>();
        // JSOUP to get links
        Document doc = Jsoup.parse(pageData);

        Elements links = doc.select("a[href]");

        for (Element link : links) {
            if (link.attr("href").startsWith("/")) {
                output.add(new Page(link.attr("href")));
            }
        }
        return output;
    }

}
