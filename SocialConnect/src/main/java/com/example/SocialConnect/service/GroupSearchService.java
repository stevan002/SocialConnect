package com.example.SocialConnect.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.SocialConnect.indexmodel.GroupIndex;
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
public class GroupSearchService {

    private final ElasticsearchOperations elasticsearchRestTemplate;

    public List<Map<String, Object>> searchGroups(String name, String description, String operator, String fileContent, Long minNumberOfPosts, Long maxNumberOfPosts) {
        if (name == null && description == null && fileContent == null && minNumberOfPosts == null && maxNumberOfPosts == null) {
            return Collections.emptyList();
        }

        // Dynamic highlighting
        List<HighlightField> fields = Arrays.asList(
                new HighlightField("name"),
                new HighlightField("description"),
                new HighlightField("file_content")
        );
        HighlightQuery highlightQuery = new HighlightQuery(new Highlight(fields), GroupIndex.class);

        Query boolQuery = buildBoolQuery(name, description, fileContent, operator, minNumberOfPosts, maxNumberOfPosts);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(boolQuery)
                .withHighlightQuery(highlightQuery)
                .build();

        SearchHits<GroupIndex> searchHits = elasticsearchRestTemplate.search(searchQuery, GroupIndex.class,
                IndexCoordinates.of("group_index"));

        List<Map<String, Object>> results = new ArrayList<>();
        for (SearchHit<GroupIndex> hit : searchHits) {
            Map<String, Object> result = new HashMap<>();
            result.put("source", hit.getContent());
            result.put("highlights", hit.getHighlightFields());
            results.add(result);
        }

        return results;
    }

    private Query buildBoolQuery(String name, String description, String fileContent, String operator, Long minNumberOfPosts, Long maxNumberOfPosts) {
        return Query.of(q -> q.bool(b -> {
            boolean useAnd = "AND".equalsIgnoreCase(operator);

            addMatchQueries(b, "name", name, useAnd);
            addMatchQueries(b, "description", description, useAnd);
            addMatchQueries(b, "file_content", fileContent, useAnd);
            addRangeQuery(b, minNumberOfPosts, maxNumberOfPosts, useAnd);

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

    private void addRangeQuery(BoolQuery.Builder b, Long minNumberOfPosts, Long maxNumberOfPosts, boolean useAnd) {
        if (minNumberOfPosts != null && maxNumberOfPosts != null) {
            String min = minNumberOfPosts.toString();
            String max = maxNumberOfPosts.toString();

            if (useAnd) {
                b.must(sb -> sb.range(r -> r.field("number_of_posts").from(min).to(max)));
            } else {
                b.should(sb -> sb.range(r -> r.field("number_of_posts").from(min).to(max)));
            }
        }
    }
}
