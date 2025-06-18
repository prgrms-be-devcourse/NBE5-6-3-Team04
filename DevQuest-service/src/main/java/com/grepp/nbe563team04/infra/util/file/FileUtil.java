package com.grepp.nbe563team04.infra.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtil {

    @Value("${upload.path}")
    private String filePath;

    @Value("${upload.achievement-path}")
    private String achievementPath;

    public List<FileDto> upload(List<MultipartFile> files, String depth) throws IOException {
        List<FileDto> fileDtos = new ArrayList<>();

        if (files.isEmpty() || files.getFirst().isEmpty()) {
            return fileDtos;
        }
        String savePath = createSavePath(depth);

        for (MultipartFile file : files) {
            String originFileName = file.getOriginalFilename();
            assert originFileName != null;
            String renameFileName = generateRenameFileName(originFileName);
            FileDto fileDto = new FileDto(originFileName, renameFileName, savePath);
            fileDtos.add(fileDto);
            uploadFile(file, fileDto, depth);
        }

        return fileDtos;
    }

    private void uploadFile(MultipartFile file, FileDto fileDto, String depth) throws IOException {
        // depth가 "achievement"이면 업적 경로에 저장
        String basePath = "achievement".equals(depth) ? achievementPath : filePath;

        File path = new File(basePath + fileDto.getSavePath());
        if (!path.exists()) {
            path.mkdirs();
        }

        File target = new File(basePath + fileDto.getSavePath() + fileDto.getRenameFileName());
        file.transferTo(target);
    }
    private String generateRenameFileName(String originFileName) {
        String ext = originFileName.substring(originFileName.lastIndexOf("."));
        return UUID.randomUUID() + ext;
    }

    private String createSavePath(String depth) {
        return "/";
    }

    public void delete(String oldPath) throws IOException {
        Path path = Path.of(filePath + oldPath);
        if (Files.exists(path)) {
            Files.delete(path);
        }

    }
}
