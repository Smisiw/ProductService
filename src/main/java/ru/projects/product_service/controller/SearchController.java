package ru.projects.product_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.projects.product_service.DTO.VariationResponseDto;
import ru.projects.product_service.service.SearchService;

@RestController
@RequestMapping("api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/{searchText}")
    public Page<VariationResponseDto> search(@PathVariable String searchText, Pageable pageable) {
        return searchService.searchProducts(searchText, pageable);
    }
}
