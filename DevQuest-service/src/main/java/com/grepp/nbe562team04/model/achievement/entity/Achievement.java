package com.grepp.nbe562team04.model.achievement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achieve_id")
    private Long achieveId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;
}