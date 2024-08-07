package com.example.SocialConnect.indexmodel;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "group_index")
@Builder
public class GroupIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "name", analyzer = "serbian", searchAnalyzer = "serbian")
    private String name;

    @Field(type = FieldType.Text, store = true, name = "description", analyzer = "serbian", searchAnalyzer = "serbian")
    private String description;

    @Field(type = FieldType.Text, store = true, name = "file_content", analyzer = "serbian", searchAnalyzer = "serbian")
    private String fileContent;

    @Field(type = FieldType.Long, store = true, name = "number_of_posts")
    private Long numberOfPosts;

    @Field(type = FieldType.Double, store = true, name = "average_likes")
    private Double averageLikes;

    @Field(type = FieldType.Long, store = true, name = "database_id")
    private Long databaseId;
}
