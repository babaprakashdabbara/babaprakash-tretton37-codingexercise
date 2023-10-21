package com.tretton37;

import com.tretton37.service.JsoupWebScraperService;
import com.tretton37.service.WebScraperService;

public class Tretton37Application {

    private static final String WEBSITE_URL = "https://books.toscrape.com/";

    public static void main(String[] args) {
        WebScraperService jsoupWebScraperService = new JsoupWebScraperService();
        jsoupWebScraperService.handleWebPageScrapping(WEBSITE_URL, "Books");
    }
}
