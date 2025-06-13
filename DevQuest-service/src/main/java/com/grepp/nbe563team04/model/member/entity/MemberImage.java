package com.grepp.nbe563team04.model.member.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class MemberImage {

    @Id
    @Column(name = "user_image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userImageId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Member member;

    private String originFileName;
    private String renameFileName;
    private String savePath;
    private LocalDateTime createdAt;
    private Boolean activated;
}