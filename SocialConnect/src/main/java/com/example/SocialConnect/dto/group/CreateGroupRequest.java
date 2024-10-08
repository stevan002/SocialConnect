package com.example.SocialConnect.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateGroupRequest {

    @NotBlank(message = "Group name is required")
    @Size(min = 4, max = 20, message = "Group name must be between 4 and 20 characters")
    private String name;

    @NotBlank(message = "Group description is required")
    private String description;

    @NotNull
    private String filename;
}
