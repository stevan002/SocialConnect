package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.group.CreateGroupRequest;
import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.repository.GroupRepository;
import com.example.SocialConnect.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/create-group")
    public ResponseEntity<?> createGroup(@RequestBody @Valid CreateGroupRequest groupRequest, Principal principal){
        String username = principal.getName();
        groupService.createGroup(groupRequest, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse(true, "Successfully created group"));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getAllGroups(){
        return ResponseEntity.ok(groupService.getAll());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{groupId}")
    public ResponseEntity<?> getGroup(@PathVariable Long groupId){
        return ResponseEntity.ok(groupService.getGroup(groupId));
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<?> deleteGroup(@PathVariable Long groupId, Principal principal){
        String username = principal.getName();
        groupService.deleteGroup(groupId, username);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Successfully deleted group"));
    }
}
