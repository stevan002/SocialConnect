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
@Table(name = "posts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 2000, message = "Content cannot exceed 2000 characters")
    @Column(nullable = false, length = 2000)
    private String content;

    @NotNull(message = "Create date cannot be null")
    @Column(nullable = false)
    private LocalDateTime createDate;

    @NotNull(message = "Posted by cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_fk_id", nullable = false)
    private User postedBy;

    @ManyToOne
    @JoinColumn(name = "group_fk_id")
    private Group group;

    @OneToMany(mappedBy = "post")
    private List<Reaction> reactions;
}
