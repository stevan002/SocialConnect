package com.example.SocialConnect.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
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

    public List<Map<String, Object>> searchPosts(String title, String fullContent, String operator) {
        if (title == null && fullContent == null) {
            return Collections.emptyList();
        }

        //Highlighter
        ArrayList<HighlightField> fields = new ArrayList<>();
        fields.add(new HighlightField("title"));
        fields.add(new HighlightField("full_content"));
        HighlightQuery highlightQuery = new HighlightQuery(new Highlight(fields), PostIndex.class);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
                    // Operator
                    boolean useAnd = "AND".equalsIgnoreCase(operator);

                    if (title != null) {

                        // FuzzyQuery
                        if (useAnd) b.must(sb -> sb.match(m -> m.field("title")
                                .fuzziness(Fuzziness.ONE.asString()).query(title)));
                        else b.should(sb -> sb.match(m -> m.field("title")
                                .fuzziness(Fuzziness.ONE.asString()).query(title)));

                        //PhrazeQuery
                        b.should(sb -> sb.matchPhrase(p -> p.field("title")
                                .slop(1).query(title)));
                    }

                    if (fullContent != null) {

                        //FuzzyQuery
                        if (useAnd) b.must(sb -> sb.match(m -> m.field("full_content")
                                .fuzziness(Fuzziness.ONE.asString()).query(fullContent)));
                        else b.should(sb -> sb.match(m -> m.field("full_content")
                                .fuzziness(Fuzziness.ONE.asString()).query(fullContent)));

                        //PhrazeQuery
                        b.should(sb -> sb.matchPhrase(p -> p.field("full_content")
                                .slop(1).query(fullContent)));
                    }

                    return b;
                })))._toQuery())
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
}
