package com.grepp.nbe563team04.model.member.dto;

import com.grepp.nbe563team04.infra.util.file.FileDto;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberImageDto {

    private Long Id;
    private Long userId;
    private String originFileName;
    private String renameFileName;
    private String savePath;
    private LocalDateTime createdAt;
    private Boolean activated;

    public MemberImageDto(Long userId, FileDto fileDto) {
        this.userId = userId;
        this.originFileName = originFileName;
        this.renameFileName = renameFileName;
        this.savePath = savePath;
    }

    public String getUrl() {
        return "/download/" + savePath + renameFileName;
    }

    public String getPath() {
        return savePath + renameFileName;
    }
}
