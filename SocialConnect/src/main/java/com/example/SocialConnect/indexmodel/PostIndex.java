package com.example.SocialConnect.indexmodel;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post_index")
@Builder
public class PostIndex {

    @Id
    private String id;

    @Field(type = FieldType.Text, store = true, name = "title", analyzer = "serbian", searchAnalyzer = "serbian")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "full_content", analyzer = "serbian", searchAnalyzer = "serbian")
    private String fullContent;

    @Field(type = FieldType.Text, store = true, name = "file_content", analyzer = "serbian", searchAnalyzer = "serbian")
    private String fileContent;

    @Field(type = FieldType.Long, store = true, name = "number_of_likes")
    private Long numberOfLikes;

    @Field(type = FieldType.Long, store = true, name = "number_of_comments")
    private Long numberOfComments;

    @Field(type = FieldType.Text, store = true, name = "comment_content", analyzer = "serbian", searchAnalyzer = "serbian")
    private List<String> commentContent = new ArrayList<>();

    @Field(type = FieldType.Long, store = true, name = "database_id")
    private Long databaseId;
}