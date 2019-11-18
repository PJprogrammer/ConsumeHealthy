package com.coderboy19.consume.healthy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class ImageCallable implements Callable<Bitmap> {

    private Document document;
    private String query;

    public ImageCallable(String query) {
        this.query = query;
    }

    @Override
    public Bitmap call() throws Exception {
        try {
            document = Jsoup.connect("https://www.pexels.com/search/" + query.replaceAll(" ", "+")).get();
        } catch (IOException e) {

        }

        Element img = document.getElementsByTag("img").get(1);
        String src = img.absUrl("src");

        if(src.equals("https://www.pexels.com/assets/pexels-sm-7627e30869d1427acc013a27df1995c0a02c4122903e4831e3ed95a31469449d.png")) {
            return null;
        }
        else {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
    }
}
