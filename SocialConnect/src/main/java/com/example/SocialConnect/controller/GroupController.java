package com.example.SocialConnect.controller;

import com.example.SocialConnect.dto.group.CreateGroupRequest;
import com.example.SocialConnect.dto.http.ApiResponse;
import com.example.SocialConnect.service.FileServiceMinio;
import com.example.SocialConnect.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;
    private final FileServiceMinio fileServiceMinio;

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/create-group", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createGroup(@RequestPart("group") @Valid CreateGroupRequest groupRequest,
                                         @RequestPart("file") MultipartFile file,
                                         Principal principal){
        System.out.println("Received request to create group with file: " + file.getOriginalFilename());
        String username = principal.getName();
        groupService.createGroup(groupRequest, username, file);
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

    @GetMapping("/file/{filename}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) throws IOException {
        var minioResponse = fileServiceMinio.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, minioResponse.headers().get("Content-Disposition"))
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(new InputStreamResource(minioResponse));
    }
}
