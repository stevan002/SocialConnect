package com.example.SocialConnect.indexrepository;

import com.example.SocialConnect.indexmodel.GroupIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupIndexRepository extends ElasticsearchRepository<GroupIndex, String> {
    Optional<GroupIndex> findByDatabaseId(Long databaseId);
    void deleteByDatabaseId(Long databaseId);
}
