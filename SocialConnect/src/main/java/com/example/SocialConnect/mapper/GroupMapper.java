package com.example.SocialConnect.mapper;

import com.example.SocialConnect.dto.group.GroupResponse;
import com.example.SocialConnect.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupResponse toGroupResponse(Group group);

    List<GroupResponse> toGroupResponseList(List<Group> groups);
}
