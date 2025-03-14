package com.mymarket.productimage;

import com.mymarket.web.error.ApplicationException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
@Transactional
public class ProductImageFileService {

    private final String webBaseDirectory;

    public ProductImageFileService(@Value("${web.base.directory}") String webBaseDirectory) {
        this.webBaseDirectory = webBaseDirectory;
    }

    public void uploadImage(Long productId, MultipartFile[] files) {
        var uploadDirectoryPath = webBaseDirectory + "/uploads/" + productId;
        new File(uploadDirectoryPath).mkdirs();
        try {
            for (MultipartFile file : files) {
                var destinationFile = Paths.get(uploadDirectoryPath)
                    .resolve(file.getOriginalFilename());
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                }
                log.info("uploaded file {} for product {}", destinationFile, productId);
            }
        } catch (IOException ex) {
            log.error("error uploading file", ex);
            throw new ApplicationException("file-upload-error");
        }
    }

    public void deleteImage(Long productId, Long imageId, String url) {
        var file = new File(webBaseDirectory + url);
        var result = file.delete();
        log.info("Deleted file for image {}, product {}, url {} with result {}", imageId, productId, url, result);
    }
}
