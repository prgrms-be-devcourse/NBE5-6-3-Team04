package com.grepp.nbe563team04.model.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserImageDto {

    private Long Id;
    private Long userId;
    private String originFileName;
    private String renameFileName;
    private String savePath;
    private LocalDateTime createdAt;
    private Boolean activated;

    public UserImageDto(Long userId, FileDto fileDto) {
        this.userId = userId;
        this.originFileName = originFileName;
        this.renameFileName = renameFileName;
        this.savePath = savePath;
    }
}
