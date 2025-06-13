package com.grepp.nbe563team04.infra.util.file;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {

    private String originFileName;
    private String renameFileName;
    private String savePath;
}
