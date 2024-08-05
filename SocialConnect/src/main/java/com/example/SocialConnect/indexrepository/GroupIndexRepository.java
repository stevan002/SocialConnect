package com.example.SocialConnect.indexrepository;

import com.example.SocialConnect.indexmodel.GroupIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupIndexRepository extends ElasticsearchRepository<GroupIndex, String> {
}
