package com.norwegiancorgi.fileserver.services;

import com.norwegiancorgi.fileserver.models.Owner;
import com.norwegiancorgi.fileserver.repositories.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class OwnerService {

    private OwnerRepository ownerRepository;
    private final Logger logger = LoggerFactory.getLogger(OwnerService.class);
    @Value("${root.location}")
    private String rootLocation;

    /**
     * Constructor
     */
    @Autowired
    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public Owner getOwnerByEmail(
            String email
    ) {
        return ownerRepository
                .findOwnerByEmail(email)
                .get();
    }

    public void init(
            Long id
    ) throws Exception{
        String currentFolder = rootLocation + id.toString();
        try {
            Files.createDirectories(Paths.get(currentFolder));
            logger.info("Directory successfully created");
        } catch (Exception e) {
            logger.error("Directory could not be created");
            throw new Exception("Could not create upload folder");
        }
    }

    public void createOwner(
            String name,
            String email,
            String password
    ) throws Exception {
        try {
            Owner owner = new Owner(
                    name,
                    email,
                    password
            );
            ownerRepository.save(owner);
            logger.info("Owner created successfully");
        } catch (Exception e) {
            logger.error("Owner could not be created");
            throw new Exception("Owner could not be created");
        }
        init(ownerRepository
                .findOwnerByEmail(email)
                .get()
                .getId());
    }

    public void updatePassword(
            Long id,
            String oldPassword,
            String newPassword
    ) throws Exception {
       try {
           Owner owner = ownerRepository.getById(id);
           if(Objects.equals(owner.getPassword(), oldPassword)) {
               ownerRepository.updatePassword(id, newPassword);
               logger.info("Password updated successfully");
           }
           else {
               throw new Exception("Cannot update password");
           }
       } catch(Exception e) {
           logger.error("Cannot update password");
           throw new Exception("Cannot update password");
       }
    }

    public void deleteUser(
            Long id,
            String password
    ) throws Exception {
        try {
            Owner owner = ownerRepository.getById(id);
            if(Objects.equals(owner.getPassword(), password)) {
                ownerRepository.deleteById(id);
                logger.info("Owner deleted successfully");
            }
            else {
                throw new Exception("Owner could not be deleted");
            }
        } catch (Exception e) {
            logger.error("Owner could not be deleted");
            throw new Exception(e.getMessage());
        }
    }
}
