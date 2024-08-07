package com.example.SocialConnect.controller;

import com.example.SocialConnect.service.GroupSearchService;
import com.example.SocialConnect.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final GroupSearchService groupSearchService;
    private final PostSearchService postSearchService;

    @GetMapping("/groups")
    public List<Map<String, Object>> searchGroups(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false) String fileContent,
                                                  @RequestParam(required = false, defaultValue = "OR") String operator) {
        return groupSearchService.searchGroups(name, description, operator, fileContent);
    }

    @GetMapping("/posts")
    public List<Map<String, Object>> searchPosts(@RequestParam(required = false) String title,
                                                  @RequestParam(required = false) String content,
                                                 @RequestParam(required = false) String fileContent,
                                                  @RequestParam(required = false, defaultValue = "OR") String operator) {
        return postSearchService.searchPosts(title, content, operator, fileContent);
    }

}
