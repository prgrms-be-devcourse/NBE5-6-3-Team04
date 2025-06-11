package com.grepp.nbe562team04.model.level.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
public class Level {

    @Id
    private Long levelId;
    private String levelName;
    private Integer xp;

}
