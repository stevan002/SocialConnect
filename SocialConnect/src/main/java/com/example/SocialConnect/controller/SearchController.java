package com.example.SocialConnect.controller;

import com.example.SocialConnect.service.GroupSearchService;
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

    @GetMapping("/groups")
    public List<Map<String, Object>> searchGroups(@RequestParam(required = false) String name,
                                                  @RequestParam(required = false) String description,
                                                  @RequestParam(required = false, defaultValue = "OR") String operator) {
        return groupSearchService.searchGroups(name, description, operator);
    }

}
