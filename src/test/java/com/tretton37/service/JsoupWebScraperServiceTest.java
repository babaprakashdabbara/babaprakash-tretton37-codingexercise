package com.tretton37.service;

import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JsoupWebScraperServiceTest {

    @Mock
    private JsoupWebScraperService jsoupWebScraperService;

    @Mock
    private Connection connection;

    @Mock
    private Document document;

    @Captor
    private ArgumentCaptor<File> fileCaptor;

    @BeforeEach
    void setUp() {
        when(jsoupWebScraperService.extractImageFileName(Mockito.anyString())).thenReturn(Mockito.mock(Matcher.class));
    }

    @Test
    void testTraverseAndDownload() throws IOException {
        Mockito.doNothing().when(jsoupWebScraperService).createDirectories(Mockito.anyString());
        when(connection.get()).thenReturn(document);
        Mockito.doNothing().when(jsoupWebScraperService).downloadImages(Mockito.any(), Mockito.anyString());
        when(document.select("li.next > a")).thenReturn(new Elements());
        when(document.html()).thenReturn("<html><head></head><body>Test Page</body></html");

        jsoupWebScraperService.traverseAndDownload("https://example.com", "test_directory");
    }

    @Test
    void testDownloadPage() throws IOException {
        when(connection.get()).thenReturn(document);
        when(document.html()).thenReturn("<html><head></head><body>Test Page</body></html");

        jsoupWebScraperService.downloadPage("https://example.com", "test_directory");
    }

    @Test
    void testDownloadImages() throws IOException {
        when(jsoupWebScraperService.getImageFileName(Mockito.anyString())).thenReturn("image.jpg");
        when(document.select("img[src]")).thenReturn(new Elements());
        Element imgElement = Mockito.mock(Element.class);
        when(imgElement.absUrl("src")).thenReturn("https://example.com/images/image.jpg");
        when(document.select("img[src]")).thenReturn(new Elements(imgElement));
        when(jsoupWebScraperService.extractImageFileName(Mockito.anyString())).thenReturn(Mockito.mock(Matcher.class));
        InputStream in = Mockito.mock(InputStream.class);
        when(in.read()).thenReturn(-1);
        when(in.read(Mockito.any())).thenReturn(-1);

        jsoupWebScraperService.downloadImages(document, "test_directory");
    }

    @Test
    void testCreateDirectories() {
        jsoupWebScraperService.createDirectories("test_directory");
    }

    @Test
    void testGetImageFileName() {
        String imageUrl = "https://example.com/images/image.jpg";
        String matcher = jsoupWebScraperService.getImageFileName(imageUrl);
    }

    @Test
    void testExtractImageFileName() {
        Matcher matcher = jsoupWebScraperService.extractImageFileName("https://example.com/images/image.jpg");
    }
}