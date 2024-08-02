package com.example.SocialConnect.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String text;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "post_fk_id", nullable = false)
    private Post post;

    @NotNull(message = "Created by cannot be null")
    @ManyToOne
    @JoinColumn(name = "user_fk_id", nullable = false)
    private User createdBy;

    @OneToMany(mappedBy = "comment")
    private List<Reaction> reactions;
}
