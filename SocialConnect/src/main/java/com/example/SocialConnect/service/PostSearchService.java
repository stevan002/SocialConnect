package com.example.SocialConnect.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.SocialConnect.indexmodel.PostIndex;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostSearchService {

    private final ElasticsearchOperations elasticsearchRestTemplate;

    public List<Map<String, Object>> searchPosts(String title, String fullContent, String operator, String fileContent) {
        if (title == null && fullContent == null && fileContent == null) {
            return Collections.emptyList();
        }

        // Dynamic highlighting
        List<HighlightField> fields = Arrays.asList(
                new HighlightField("title"),
                new HighlightField("full_content"),
                new HighlightField("file_content")
        );
        HighlightQuery highlightQuery = new HighlightQuery(new Highlight(fields), PostIndex.class);

        Query boolQuery = buildBoolQuery(title, fullContent, fileContent, operator);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(boolQuery)
                .withHighlightQuery(highlightQuery)
                .build();

        SearchHits<PostIndex> searchHits = elasticsearchRestTemplate.search(searchQuery, PostIndex.class,
                IndexCoordinates.of("post_index"));

        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit<PostIndex> hit : searchHits) {
            Map<String, Object> result = new HashMap<>();
            result.put("source", hit.getContent());
            result.put("highlights", hit.getHighlightFields());
            results.add(result);
        }

        return results;
    }

    private Query buildBoolQuery(String title, String fullContent, String fileContent, String operator) {
        return Query.of(q -> q.bool(b -> {
            boolean useAnd = "AND".equalsIgnoreCase(operator);

            addMatchQueries(b, "title", title, useAnd);
            addMatchQueries(b, "full_content", fullContent, useAnd);
            addMatchQueries(b, "file_content", fileContent, useAnd);

            return b;
        }));
    }

    private void addMatchQueries(BoolQuery.Builder b, String field, String value, boolean useAnd) {
        if (value != null) {
            if (useAnd) {
                b.must(mb -> mb.match(m -> m.field(field)
                        .fuzziness(Fuzziness.ONE.asString()).query(value)));
            } else {
                b.should(sb -> sb.match(m -> m.field(field)
                        .fuzziness(Fuzziness.ONE.asString()).query(value)));
            }

            b.should(sb -> sb.matchPhrase(mp -> mp.field(field)
                    .slop(1).query(value)));
        }
    }
}
