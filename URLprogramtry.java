/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mypackage;

import java.io.File;
import java.io.PrintWriter;import java.net.URI;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author nikhi
 */
public class URLprogramtry {

    public static void Programcontent() throws Exception {
        Queue<String> linkstobevisited = new PriorityQueue<String>();
        HashSet<String> visited = new HashSet<String>();
        //starting the web crawling with the link "http://www.unt.edu"
        String root = "http://www.unt.edu";
        int filecount = 0;
        int linkscount = 1;
        int cout=0;
        linkstobevisited.offer(root);
        // This is the second url link added to be visited so that when given professor link they provide useful links
        linkstobevisited.offer("http://www.cse.unt.edu/~ccaragea");
        //when the linkstobevisited becomes empty this lopp stops
        while (!linkstobevisited.isEmpty()) {
            //getting thelink that has to be visited
        String currentUrl = linkstobevisited.poll();
          
            visited.add(currentUrl);
           Document doc = null;
           try {
               //making a connection to the url
                Connection conn = Jsoup.connect(currentUrl);
                Connection.Response resp = conn.userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10000)
                        .execute();
                //checking the status code of the url
                if (resp.statusCode() != 200) {
                    continue;
                }
                // getting the body text which is to be used for weighting 
                doc = conn.get();
                String text = doc.body().text();
               URI obj = new URI(currentUrl);
               //coniditon to select the valid URL 
                if (!obj.getHost().toString().contains("unt.edu") || text == null||(currentUrl.contains("calendar") ||(obj.getHost().toString().contains("maps"))||(currentUrl.contains("facebook"))||(currentUrl.contains("twitter"))||(obj.getHost().toString().contains("blogs"))||(currentUrl.contains("events")))) {
                    continue;
                }
                //getting the document title
                String title = doc.title();
                
                //downloading the files in the specified folder
                String path = "C:" + File.separator + "hello" + File.separator + filecount + ".txt";
                File f = new File(path);

                f.getParentFile().mkdirs();
                f.createNewFile();
                try (PrintWriter printwritefile = new PrintWriter(f)) {
                    printwritefile.println(filecount);
                    printwritefile.println(currentUrl);
                    printwritefile.println(title);
                    printwritefile.println(text);
                    printwritefile.close();
                }
            } catch (Exception e) {
                continue;
            }

            filecount++;
            if (doc == null) {
                continue;
            }
            // getting the ahref liks from the URL
            Elements links = doc.select("a[href]");

            for (Element link : links) {
              String urlcombine = link.attr("href");
              //if the link starts with / then we append with the base url
                if (urlcombine.startsWith("/")) {
                    urlcombine = root + urlcombine;
                }
                // if the url has / at the end, we remove it and store it
                urlcombine = urlcombine.substring(0, urlcombine.length() - (urlcombine.endsWith("/") ? 1 : 0));
                urlcombine = urlcombine.replaceAll("https://", "http://");
                
                if (urlcombine.contains("#")) {
                    continue;
                }
                //if the visited doesnt contain the valid url then it has to be added 
                if (!visited.contains(urlcombine)) {
                   linkstobevisited.add(urlcombine);
                    visited.add(urlcombine);
                    cout++;
                }
            }
            linkscount++;
            // this loop continues until 5000 files have been downloaded
            if (linkscount > 5000) {
                break;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Programcontent();
    }
}
