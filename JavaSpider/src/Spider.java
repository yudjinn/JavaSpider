package JavaSpider.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Spider {
    private static final Logger log = Logger.getLogger(Spider.class.getName());
    private static HTTPConnection httpConnection;
    private static WebPageQueue queue;
    private static WebPageParser parser;
    private static String[] adminPages;
    public final String hostName;

    public Spider(String hostName, Integer port) {
        this.hostName = hostName;
        httpConnection = new HTTPConnection(hostName, port);
        queue = new WebPageQueue("/");
        parser = new WebPageParser();
        // hard coded admin pages to crawl
        adminPages = new String[] { "cPage=index&amp;actionId=FLUSH_CACHE", "cPage=stats&amp;actionId=FLUSH_CACHE",
                "cPage=index&amp;actionId=SCHEDULE_JOB_INSTANCE&amp;organizationId=SMT",
                "cPage=index&amp;actionId=WEB_SOCKET&amp;organizationId=SMT",
                "cPage=index&amp;actionId=ERROR_LOG&amp;organizationId=SMT" };
    }

    public static void main(String[] args) {
        Spider spider = new Spider("www.siliconmtn.com", 443);
        httpConnection.head("/");
        spider.processPublic(queue);
        spider.processAdmin(queue);
        // save historical list of visited pages
        queue.saveHistorical();
    }

    // Crawl public pages
    public void processPublic(WebPageQueue q) {
        while (q.hasPages()) {
            Page p = q.popPage();
            log.info("trying " + p.getPath());
            HTTPResponse response = httpConnection.get(p.getPath());
            // get Response data and save to page
            p.setData(response.body);
            // Save page
            p.save();
            for (Page page : parser.getLinks(p.getData())) {
                q.addPage(page);
            }
        }
    };

    // Crawl admintool for specific pages
    public void processAdmin(WebPageQueue q) {
        q.addPage(new Page("/sb/admintool"));
        Page p = q.popPage();

        HTTPResponse response = httpConnection.post(p.getPath(), loginData());
        p.setData(response.body);
        p.save();
        for (String path : this.adminPages) {
            q.addPage(new Page("/sb/admintool?" + path));
        }

        // Duplicate functionality, but needed to break out adding links
        while (q.hasPages()) {
            p = q.popPage();
            log.info("trying " + p.getPath());
            response = httpConnection.get(p.getPath());
            // get Response data and save to page
            p.setData(response.body);
            // Save page
            p.save();

        }
    }

    /**
     * Create map for logindata in post request
     * 
     * @return Map logindata
     */
    public static Map<String, String> loginData() {
        Map<String, String> loginData = new HashMap<>();
        loginData.put("requestType", "reqBuild");
        loginData.put("pmid", "ADMIN_LOGIN");
        loginData.put("emailAddress", "EMAIL_ADDRESS"); // Fill this in with uri-encoded emailaddress
        loginData.put("password", "PASSWORD"); // Fill this in with uri-encoded password
        loginData.put("l", "");
        return loginData;
    }

}
