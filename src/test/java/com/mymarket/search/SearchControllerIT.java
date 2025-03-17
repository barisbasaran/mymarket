package com.mymarket.search;

import com.mymarket.base.ElasticsearchAndPostgreBase;
import com.mymarket.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class SearchControllerIT extends ElasticsearchAndPostgreBase {

    @Autowired
    private IndexService indexService;

    @Autowired
    private TestRestTemplate template;

    @Test
    public void search() {
        indexService.indexProducts();
        var product1 = Product.builder().id(1L).name("a1").description("").build();
        var product2 = Product.builder().id(2L).name("b1").description("aa").build();
        var product3 = Product.builder().id(3L).name("c1").description("").build();
        indexService.updateProduct(product1);
        indexService.updateProduct(product2);
        indexService.updateProduct(product3);

        var response = template.getForEntity("/search?query=a", SearchResult.class);
        var searchResult = response.getBody();

        assertThat(searchResult).isNotNull();
        assertThat(searchResult.getTotalHits()).isEqualTo(2);
        assertThat(searchResult.getItems().get(0).getId()).isEqualTo("product-1");
        assertThat(searchResult.getItems().get(1).getId()).isEqualTo("product-2");
    }
}
