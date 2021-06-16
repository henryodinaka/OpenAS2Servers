package connect.as2.as2middleware.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import connect.as2.as2middleware.config.APIException;
import connect.as2.as2middleware.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class FileService {

    @Value("${path.to.anyPartner}")
    private String pathToAnyPartner;
    @Value("${path.mdn}")
    private String mdnPath;

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(pathToAnyPartner));
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload folder!");
        }
    }

    public void save(MultipartFile file) {
        try {
            Path root = Paths.get(pathToAnyPartner);
            if (!Files.exists(root)) {
                init();
            }
            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource loadFileByName(String filename) {
        try {
            Path file = Paths.get(pathToAnyPartner)
                    .resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteAll(String folderName) {
        FileSystemUtils.deleteRecursively(Paths.get(folderName)
                .toFile());
    }
    public void deleteFile(String fileName) {
        FileSystemUtils.deleteRecursively(Paths.get(pathToAnyPartner)
                .toFile());
    }

    public List<File> loadAllMDN() {
        try {
            mdnPath = mdnPath+"\\"+LocalDate.now();
            log.info("path {}",mdnPath);
            Path root = Paths.get(mdnPath);
            if (Files.exists(root)) {
                var fileList = Files.walk(root, 1)
                        .filter(path -> !path.equals(root)).map(p -> p.toFile())
                        .collect(Collectors.toList());
                for (File file : fileList){
                    log.info("File Received {}",file.getName());
                }
                return fileList;
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }
}