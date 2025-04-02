package lk.ijse.aadfinalproject_auctionsite_.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    private final String UPLOAD_DIR = "uploads/";

    public String saveFile(MultipartFile file) {
        if (file.isEmpty()) {
            return null;
        }
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            file.transferTo(new File(filePath.toString()));
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
