package by.rssreader.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

import by.rssreader.models.Rss;
import by.rssreader.models.RssList;

public class RssParser implements Parser<RssList> {

    private RssList rssList = new RssList();

    public RssList parse(InputStream in, URL url) throws Exception {

        Document doc = Jsoup.parse(in, null, url.toString(), org.jsoup.parser.Parser.xmlParser());
        for (Element element : doc.getElementsByTag("item")) {
            Rss item = new Rss();
            item.setTitle(Jsoup.parse(element.selectFirst("title").text()).text());
            item.setLink(element.selectFirst("link").text());
            item.setDescription(Jsoup.parse(element.selectFirst("description").text()).text());
            item.setPubDate(new Date(element.selectFirst("pubDate").text()));
            Element imageUrl = element.getElementsByAttribute("url").first();
            if (Objects.nonNull(imageUrl))
                item.setImageUrl(imageUrl.attr("url"));
            rssList.addItem(item);
        }

        return rssList;
    }
}
