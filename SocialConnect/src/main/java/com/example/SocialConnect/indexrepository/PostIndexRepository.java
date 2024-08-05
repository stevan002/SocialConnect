package com.example.SocialConnect.indexrepository;

import com.example.SocialConnect.indexmodel.PostIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostIndexRepository extends ElasticsearchRepository<PostIndex, String> {
}
