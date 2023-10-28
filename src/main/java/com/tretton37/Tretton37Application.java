package com.tretton37;

import com.tretton37.service.JsoupWebScraperService;
import com.tretton37.service.WebScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Tretton37Application {

    private static final String WEBSITE_URL = "https://books.toscrape.com/";
    private static final Logger logger = LoggerFactory.getLogger(Tretton37Application.class);

    public static void main(String[] args) throws IOException {
        try {
            WebScraperService jsoupWebScraperService = new JsoupWebScraperService();
            jsoupWebScraperService.handleWebPageScrapping(WEBSITE_URL, "Books");
        } catch (IOException exception) {
            logger.error("Error while performing web pages scraping {}", exception.getMessage());
            throw exception;
        }
    }
}
