package ru.projects.product_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.projects.product_service.model.ProductDoc;

import java.util.UUID;

public interface ProductElasticsearchRepository extends ElasticsearchRepository<ProductDoc, UUID> {
    @Query("""
            {
              "bool": {
                "should": [
                  {
                    "multi_match": {
                      "query": "?0",
                      "fields": ["name^4", "description^3"],
                      "type": "best_fields",
                      "operator": "or"
                    }
                  },
                  {
                    "match_phrase": {
                      "name": {
                        "query": "?0",
                        "boost": 3
                      }
                    }
                  },
                  {
                    "match_phrase": {
                      "description": {
                        "query": "?0",
                        "boost": 2
                      }
                    }
                  }
                ],
                "minimum_should_match": 1
              }
            }
            """)
    Page<ProductDoc> searchByQuery(String query, Pageable pageable);
}
