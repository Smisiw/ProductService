package ru.projects.product_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ElasticsearchInitializer implements ApplicationRunner {

    private final SearchService searchService;

    @Override
    public void run(ApplicationArguments args) {
        searchService.initIndexes();
    }
}