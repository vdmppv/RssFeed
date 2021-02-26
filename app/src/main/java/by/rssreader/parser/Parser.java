package by.rssreader.parser;

import java.io.InputStream;
import java.net.URL;

public interface Parser<T> {
    T parse(InputStream in, URL url) throws Exception;
}
