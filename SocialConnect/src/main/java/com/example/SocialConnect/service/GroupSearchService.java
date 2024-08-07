package com.example.SocialConnect.service;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
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

    public List<Map<String, Object>> searchGroups(String name, String description, String operator) {
        if (name == null && description == null) {
            return Collections.emptyList();
        }

        // Dinamički sažetak
        ArrayList<HighlightField> fields = new ArrayList<>();
        fields.add(new HighlightField("name"));
        fields.add(new HighlightField("description"));
        HighlightQuery highlightQuery = new HighlightQuery(new Highlight(fields), GroupIndex.class);

        NativeQuery searchQuery = new NativeQueryBuilder()
                .withQuery(BoolQuery.of(q -> q.must(mb -> mb.bool(b -> {
                    // Upotreba AND ili OR operatora
                    boolean useAnd = "AND".equalsIgnoreCase(operator);

                    if (name != null) {
                        if (useAnd) b.must(sb -> sb.match(m -> m.field("name")
                                .fuzziness(Fuzziness.ONE.asString()).query(name)));
                        else b.should(sb -> sb.match(m -> m.field("name")
                                .fuzziness(Fuzziness.ONE.asString()).query(name)));

                        b.should(sb -> sb.matchPhrase(p -> p.field("name")
                                .slop(1).query(name)));
                    }

                    if (description != null) {
                        if (useAnd) b.must(sb -> sb.match(m -> m.field("description")
                                .fuzziness(Fuzziness.ONE.asString()).query(description)));
                        else b.should(sb -> sb.match(m -> m.field("description")
                                .fuzziness(Fuzziness.ONE.asString()).query(description)));

                        b.should(sb -> sb.matchPhrase(p -> p.field("description")
                                .slop(1).query(description)));
                    }

                    return b;
                })))._toQuery())
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
}
