package com.example.SocialConnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "group_post")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(nullable = false, length = 1000)
    private String description;

    @NotNull(message = "Creation date cannot be null")
    @Column(nullable = false)
    private LocalDateTime creationDate;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Post> posts;

    @NotNull(message = "Created by cannot be null")
    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
}
