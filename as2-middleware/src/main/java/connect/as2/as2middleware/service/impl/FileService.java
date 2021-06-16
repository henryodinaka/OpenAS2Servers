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
import java.util.Objects;
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

    @Value("#{'${partners.name}'.split(',')}")
    private List<String> partnerNames;
    @Value("${path.storageBaseDir}")
    private String baseDir;

    @Value("${path.mdn}")
    private String mdnBasePath;
    @Value("${path.inbox}")
    private String inboxPath;

    @PostConstruct
    public void init() {
        for (String name : partnerNames) {
            String path = baseDir + "/to" + name;
            try {
                Files.createDirectories(Paths.get(path));
                log.info("created folder {}",path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create partners folder!");
            }
        }
    }

    public void save(String partnerName, MultipartFile file) {
        var pathToAnyPartner = generatePartnerPath(partnerName);
        try {
            Path root = Paths.get(pathToAnyPartner);
            if (!Files.exists(root)) {
                init();
            }
            Files.copy(file.getInputStream(), root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource loadFileByName(String filename) {
        var pathToAnyPartner = "";
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

    public String generatePartnerPath(String partnerName) {
        String pathToAnyPartner = null;
        for (String name : partnerNames) {
            if (name.equals(partnerName)) {
                pathToAnyPartner = baseDir + "/to" + name;
                break;
            }
        }
        if (pathToAnyPartner == null)
            throw new APIException(new Response(400, "16", "Partner " + partnerName + " is not configure to receiver message yet"));
        return pathToAnyPartner;
    }

    public void deleteAll(String folderName) {
        FileSystemUtils.deleteRecursively(Paths.get(folderName)
                .toFile());
    }

    public void deleteFile(String fileName) {
        String pathToAnyPartner = "";
        FileSystemUtils.deleteRecursively(Paths.get(pathToAnyPartner)
                .toFile());
    }

    public List<File> loadAllMDN() {
        try {
            String mdnPath = mdnBasePath + "/" + LocalDate.now();
            log.info("path {}", mdnPath);
            Path root = Paths.get(mdnPath);
            if (Files.exists(root)) {
                var fileList = Files.walk(root, 1)
                        .filter(path -> !path.equals(root)).map(Path::toFile)
                        .collect(Collectors.toList());
                for (File file : fileList) {
                    log.info("MDN File Received {}", file.getName());
                }
                return fileList;
            }

            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }

    public List<File> loadInbox() {
        try {
            log.info("inbox path {}", inboxPath);
            Path root = Paths.get(inboxPath);
            if (Files.exists(root)) {
                var fileList = Files.walk(root, 1)
                        .filter(path -> !path.equals(root)).map(Path::toFile)
                        .collect(Collectors.toList());
                for (File file : fileList) {
                    log.info("Inbox File Received {}", file.getName());
                }
                return fileList;
            }
            return Collections.emptyList();
        } catch (IOException e) {
            throw new RuntimeException("Could not list the files!");
        }
    }

    public void listf(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        // Get all files from a directory.
        File[] fList = directory.listFiles();
        if(fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listf(file.getAbsolutePath(), files);
                }
            }
    }
}