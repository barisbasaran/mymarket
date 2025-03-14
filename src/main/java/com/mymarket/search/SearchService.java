package com.mymarket.search;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class SearchService {

    private final ElasticsearchOperations operations;

    public SearchResult search(String term, Pageable pageable) {
        var items = term.trim().split("[ \\t]+");
        Criteria criteria = null;
        for (int i = 0; i < items.length; i++) {
            if (i == 0) {
                criteria = getCriteria(items[i]);
            } else {
                criteria = criteria.or(getCriteria(items[i]));
            }
        }
        var query = new CriteriaQuery(criteria);
        query.setPageable(pageable);
        var searchHits = operations.search(query, SProduct.class);
        return SearchResult.builder()
            .totalHits(searchHits.getTotalHits())
            .items(searchHits.stream().map(SearchHit::getContent).toList())
            .build();
    }

    private Criteria getCriteria(String text) {
        return new Criteria("name").contains(text).or(new Criteria("keywords").contains(text));
    }
}
