# Web Scraper Service

This repository handles web scraping of "https://books.toscrape.com/" and the business logic written for Web scraping
traverses all pages on "https://books.toscrape.com/" recursively and downloads and saves all files (pages, images...)
to disk while keeping the file structure.

Currently, Web scraping implementation is using Jsoup Library.

# Running the service

1. To run the service, please execute following command in the command prompt:`./gradlew run'`
2. All the downloaded files will be stored under Books directory.