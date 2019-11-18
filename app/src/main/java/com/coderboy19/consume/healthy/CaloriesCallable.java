package com.coderboy19.consume.healthy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.concurrent.Callable;

public class CaloriesCallable implements Callable<String> {
    private String query;
    private Document document;

    public CaloriesCallable(String query) {
        this.query = query;
    }

    @Override
    public String call() throws Exception {
        try {
            document = Jsoup.connect("https://www.google.com/search?q=" + query.replaceAll(" ", "+") + "+calories").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements elements = document.select("div[aria-live]");

        if(elements.isEmpty())
            return "NA";
        else
            return elements.get(0).text();
    }
}
