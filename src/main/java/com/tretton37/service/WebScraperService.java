package com.tretton37.service;

import java.io.IOException;

public interface WebScraperService {
    void handleWebPageScrapping(String url, String pathToDownload) throws IOException;
}
