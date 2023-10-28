package com.tretton37.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JsoupWebScraperServiceTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void handleWebPageScrapping() {
        String mockPageContent = "<html><body><h1>Test Page</h1></body></html>";
        String url = "http://localhost:" + wireMockServer.port() + "/test-page";
        String pathToDownload = "Books";
        WireMock.stubFor(
                WireMock.get(WireMock.urlEqualTo("/test-page"))
                        .willReturn(WireMock.aResponse()
                                .withHeader("Content-Type", "text/html")
                                .withBody(mockPageContent)
                        )
        );
        WebScraperService scraper = new JsoupWebScraperService();
        Assertions.assertDoesNotThrow(() -> scraper.handleWebPageScrapping(url, pathToDownload));
    }
}