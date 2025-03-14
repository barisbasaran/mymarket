package com.mymarket.search;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Data
@Builder
@Document(indexName = "product-index")
public class SProduct {

	private @Id String id;
	private String name;
	private List<String> keywords;
	private String url;
}
