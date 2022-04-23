package com.norwegiancorgi.fileserver.controllers;

import com.norwegiancorgi.fileserver.models.Filez;
import com.norwegiancorgi.fileserver.services.FilezService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/filez")
public class FilezController {

    private FilezService filezService;

    /**
     * Constructor
     */
    @Autowired
    public FilezController(FilezService filezService) {
        this.filezService = filezService;
    }

    /**
     * Fetch all files for the logged-in User
     */
    @GetMapping
    public ResponseEntity<List<Filez>> getAllFiles(
            @RequestParam("ownerId") Long ownerId
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(filezService.getAllFiles(ownerId));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * Download a file
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("id") Long id,
            HttpServletRequest httpServletRequest
    ) {
        try {
            Resource resource = filezService.getResource(id, ownerId);
            String contentType = filezService.getContentType(resource, httpServletRequest);
            String filename = filezService.getFileName(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Disposition", String.format("attachment; filename=\"" + filename + "\""))
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * Upload a file
     */
    @PostMapping
    public ResponseEntity<String> upload(
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("file") MultipartFile multipartFile
    ) {
        try {
            filezService.upload(multipartFile, ownerId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(multipartFile.getOriginalFilename() + " uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(e.getMessage());
        }
    }

    /**
     * Delete a file
     */
    @DeleteMapping
    public ResponseEntity<String> delete(
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("id") Long id
    ) {
        try{
            filezService.delete(id, ownerId);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("File deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(e.getMessage());
        }
    }
}
