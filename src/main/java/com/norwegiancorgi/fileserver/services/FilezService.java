package com.norwegiancorgi.fileserver.services;

import com.norwegiancorgi.fileserver.models.Filez;
import com.norwegiancorgi.fileserver.repositories.FilezRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FilezService {

    private FilezRepository filezRepository;
    private final Logger logger = LoggerFactory.getLogger(FilezService.class);
    @Value("${root.location}")
    private String rootLocation;

    /**
     * Constructor
     */
    @Autowired
    public FilezService(FilezRepository filezRepository) {
        this.filezRepository = filezRepository;
    }

    public List<Filez> getAllFiles(
            Long ownerId
    ) throws Exception {
        try {
            List<Filez> files = filezRepository.findByOwner(ownerId);
            logger.info("Files fetched successfully");
            return files;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public Resource getResource(
            Long id,
            Long ownerId
    ) throws Exception {
        String currentUserFolder = rootLocation + ownerId.toString();
        try {
            String name = filezRepository
                    .getById(id)
                    .getName();
            Path path = Paths
                    .get(currentUserFolder)
                    .resolve(name);
            Resource resource = new UrlResource(path.toUri());
            Integer numberOfDownloads = filezRepository
                    .getById(id)
                    .getNumberOfDownloads() + 1;
            if(resource.exists()) {
                filezRepository.updateNumberOfDownloads(id, numberOfDownloads);
                return resource;
            }
            else {
                logger.info("File could not be downloaded");
                throw new Exception("Could not download the file: " + name);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public String getContentType(
            Resource resource,
            HttpServletRequest httpServletRequest
    ) throws Exception {
        String contentType = null;
        try {
            contentType = httpServletRequest
                    .getServletContext()
                    .getMimeType(resource
                            .getFile()
                            .getAbsolutePath());
        } catch (Exception e) {
            throw new Exception("Could not determine file type!!!");
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return contentType;
    }

    public String getFileName(
            Long id
    ) {
        return filezRepository
                .getById(id)
                .getName();
    }

    public void upload(
            MultipartFile multipartFile,
            Long ownerId
    ) throws Exception {
        String currentUserFolder = rootLocation + ownerId.toString();
        try {
            Path path = Paths.get(currentUserFolder);
            File file = new File(multipartFile.getOriginalFilename());
            Files.copy(
                    multipartFile.getInputStream(),
                    path.resolve(multipartFile.getOriginalFilename())
            );
            Filez filez = new Filez(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    file.getAbsolutePath().replace(multipartFile.getOriginalFilename(), ""),
                    0,
                    multipartFile.getSize(),
                    ownerId
            );
            filezRepository.save(filez);
            logger.info("File successfully uploaded");
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }

    public void delete(
            Long id,
            Long ownerId
    ) throws Exception {
        String currentUserFolder = rootLocation + ownerId.toString();
        try {
            Filez filez = filezRepository.getById(id);
            Path uploadPath = Paths.get(currentUserFolder);
            String filename = filez.getName();
            File file = new File(String.valueOf(uploadPath.resolve(filename)));
            if (file.delete()) {
                filezRepository.deleteById(id);
                if(filezRepository.findById(id).isPresent()) {
                    throw new Exception("File could not be deleted!!!");
                }
                logger.info("File successfully deleted");
            } else {
                throw new Exception("File could not be deleted!!!");
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}
