package com.example.SocialConnect.service;

import com.example.SocialConnect.dto.group.CreateGroupRequest;
import com.example.SocialConnect.dto.group.GroupResponse;
import com.example.SocialConnect.exception.BadRequestException;
import com.example.SocialConnect.mapper.GroupMapper;
import com.example.SocialConnect.model.Group;
import com.example.SocialConnect.model.User;
import com.example.SocialConnect.repository.GroupRepository;
import com.example.SocialConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;


    public void createGroup(CreateGroupRequest groupRequest, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "Not found username in jwt token"));

        Group group = Group.builder()
                .name(groupRequest.getName())
                .description(groupRequest.getDescription())
                .creationDate(LocalDateTime.now())
                .createdBy(user)
                .build();

        groupRepository.save(group);
    }

    public void deleteGroup(Long groupId, String username) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BadRequestException("group", "Not found group with given id"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException("username", "Not found username in jwt token"));

        if (!group.getCreatedBy().equals(user)) {
            throw new BadRequestException("group", "Not access to delete group for logged user");
        }

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
}
