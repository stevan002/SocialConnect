package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.group.CreateGroupRequest;
import com.example.SocialConnect.dto.group.GroupResponse;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.indexmodel.GroupIndex;
import com.example.SocialConnect.indexrepository.GroupIndexRepository;
import com.example.SocialConnect.mapper.GroupMapper;
import com.example.SocialConnect.model.Group;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.GroupRepository;
import com.example.SocialConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final FileServiceMinio fileServiceMinio;
    private final GroupIndexRepository groupIndexRepository;


    public void createGroup(CreateGroupRequest groupRequest, String username, MultipartFile file) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "Not found username in jwt token"));

        String filename = fileServiceMinio.store(file, UUID.randomUUID().toString());

        Group group = Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .creationDate(LocalDateTime.now())
                .createdBy(user)
                .descriptionFilename(filename)
                .build();

        groupRepository.save(group);

        System.out.println(group.getId().toString());

        GroupIndex index = GroupIndex.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .fileContent(extractDocumentContent(file))
                .numberOfPosts(0L)
                .averageLikes(0.0)
                .databaseId(group.getId())
                .build();

        groupIndexRepository.save(index);
    }

    public void deleteGroup(Long groupId, String username) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException("group", "Not found group with given id"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "Not found username in jwt token"));

        if (!group.getCreatedBy().equals(user)) {
            throw new BadRequestException("group", "Not access to delete group for logged user");
        }
        groupIndexRepository.deleteByDatabaseId(groupId);
        groupRepository.delete(group);
    }

    public List<GroupResponse> getAll() {
         List<Group> groups = groupRepository.findAll();
         return GroupMapper.INSTANCE.toGroupResponseList(groups);
    }

    public GroupResponse getGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException("group", "Not found group with given id"));

        return GroupMapper.INSTANCE.toGroupResponse(group);
    }

    private String extractDocumentContent(MultipartFile multipartPdfFile) {
        String documentContent;
        try (InputStream pdfFile = multipartPdfFile.getInputStream()) {
            PDDocument pdDocument = PDDocument.load(pdfFile);
            PDFTextStripper textStripper = new PDFTextStripper();
            documentContent = textStripper.getText(pdDocument);
            pdDocument.close();
        } catch (IOException e) {
            throw new BadRequestException("loadPdf", "Error while trying to load PDF file content for group");
        }

        return documentContent;
    }
}
