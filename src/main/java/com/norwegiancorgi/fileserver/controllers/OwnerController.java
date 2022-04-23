package com.norwegiancorgi.fileserver.controllers;

import com.norwegiancorgi.fileserver.models.Owner;
import com.norwegiancorgi.fileserver.services.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/owner")
public class OwnerController {

    private OwnerService ownerService;

    /**
     * Constructor
     */
    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    /**
     * Fetch all users
     */
    @GetMapping
    public ResponseEntity<Owner> getUser(
            @RequestParam("email") String email
    ) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(ownerService.getOwnerByEmail(email));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * Create a new User
     */
    @PostMapping
    public ResponseEntity<String> createNewUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password
    ) {
        try {
            ownerService.createOwner(username, email, password);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Owner created successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    /**
     * Update password
     */
    @PutMapping
    public ResponseEntity<String> updatePassword(
            @RequestParam("id") Long id,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword
    ) {
        try {
            ownerService.updatePassword(id, oldPassword, newPassword);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Password updated successfully");
        } catch (Exception e) {
            return  ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    /**
     * Delete User
     */
    @DeleteMapping
    public ResponseEntity<String> deleteUser(
            @RequestParam("id") Long id,
            @RequestParam("password") String password
    ) {
        try {
            ownerService.deleteUser(id, password);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("Owner deleted successfully");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Owner could not be deleted");
        }
    }
}
