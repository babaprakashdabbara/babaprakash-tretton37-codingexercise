package com.tretton37.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsoupWebScraperService implements WebScraperService {

    private static final Logger logger = LoggerFactory.getLogger(JsoupWebScraperService.class);

    @Override
    public void handleWebPageScrapping(String url, String pathToDownload) {
        try {
            //TODO Fix File Structure
            Document document = Jsoup.connect(url).get();
            String relativePath = url.replace(url, "");
            String filePath = pathToDownload + relativePath;
            createDirectories(filePath);
            downloadPage(url, filePath);
            downloadImages(document, filePath);
            Elements nextPageLinks = document.select("li.next > a");
            if (!nextPageLinks.isEmpty()) {
                String nextPageUrl = url.substring(0, url.lastIndexOf("/") + 1) + Objects.requireNonNull(nextPageLinks.first()).attr("href");
                handleWebPageScrapping(nextPageUrl, pathToDownload);
                logger.info("Completed downloading of images for the following url {}", url);
            }
        } catch (IOException exception) {
            logger.error("Error while performing web pages scraping on following url {}, {}", url, exception.getMessage());
        }
    }

    private void downloadPage(String url, String filePath) {
        try {
            Document document = Jsoup.connect(url).get();
            File file = new File(filePath + "/index.html");
            FileWriter writer = new FileWriter(file);
            writer.write(document.html());
            writer.close();
        } catch (IOException ioException) {
            logger.error("Error while trying to download the pages {}", ioException.getMessage());
        }
    }

    private void downloadImages(Document document, String filePath) {
        Elements imgElements = document.select("img[src]");
        for (Element img : imgElements) {
            String imageUrl = img.absUrl("src");
            String imgName = getImageFileName(imageUrl);

            try (InputStream in = new URL(imageUrl).openStream()) {
                Path imgPath = Path.of(filePath, imgName);
                Files.copy(in, imgPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioException) {
                logger.error("Error while trying to download the images {}", ioException.getMessage());
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDirectories(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private String getImageFileName(String imageUrl) {
        Matcher matcher = extractImageFileName(imageUrl);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "image.jpg";
    }

    private Matcher extractImageFileName(String imageUrl) {
        Pattern pattern = Pattern.compile(".*/(.*?)$");
        return pattern.matcher(imageUrl);
    }
}

