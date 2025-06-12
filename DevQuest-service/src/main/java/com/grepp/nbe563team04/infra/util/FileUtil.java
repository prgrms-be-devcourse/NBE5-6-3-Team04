package com.grepp.nbe563team04.infra.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtil {

    @Value("${upload.path}")
    private String filePath;

    public List<FileDto> upload(List<MultipartFile> files, String depth) throws IOException {
        List<FileDto> fileDtos = new ArrayList<>();

        if (files.isEmpty() || files.getFirst().isEmpty()) {
            return fileDtos;
        }
        String savePath = createSavePath(depth);

        for (MultipartFile file : files) {
            String originFileName = file.getOriginalFilename();
            String renameFileName = generateRenameFileName(originFileName);
            FileDto fileDto = new FileDto(originFileName, renameFileName, savePath);
            fileDtos.add(fileDto);
            uploadFile(file, fileDto);
        }

        return fileDtos;
    }

    private void uploadFile(MultipartFile file, FileDto fileDto) throws IOException {
        File path = new File(filePath + fileDto.savePath());
        if (!path.exists()) {
            path.mkdirs();
        }

        File target = new File(filePath + fileDto.savePath() + fileDto.renameFileName());
        file.transferTo(target);
    }

    private String generateRenameFileName(String originFileName) {
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + ext;
    }

    private String createSavePath(String depth) {
        LocalDate now = LocalDate.now();
        return depth + "/" +
                now.getYear() + "/" +
                now.getMonth() + "/" +
                now.getDayOfMonth() + "/";
    }

    public void delete(String oldPath) throws IOException {
        Path path = Path.of(filePath + oldPath);
        if (Files.exists(path)) {
            Files.delete(path);
        }

    }
}
