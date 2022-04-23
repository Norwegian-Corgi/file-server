package com.norwegiancorgi.fileserver.models;

import javax.persistence.*;

@Entity
@Table
public class Filez {

    @Id
    @SequenceGenerator(
            name = "fileUuid",
            sequenceName = "fileUuid",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fileUuid")
    private Long id;
    private String name;
    private String type;
    private String path;
    private Integer numberOfDownloads;
    private Long size;
    private Long ownerId;

    /**
     * Constructors
     */
    public Filez() {
    }

    public Filez(Long id, String name, String type, String path, Integer numberOfDownloads, Long size, Long ownerId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.path = path;
        this.numberOfDownloads = numberOfDownloads;
        this.size = size;
        this.ownerId = ownerId;
    }

    public Filez(String name, String type, String path, Integer numberOfDownloads, Long size, Long ownerId) {
        this.name = name;
        this.type = type;
        this.path = path;
        this.numberOfDownloads = numberOfDownloads;
        this.size = size;
        this.ownerId = ownerId;
    }

    /**
     * Getters and Setters
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getNumberOfDownloads() {
        return numberOfDownloads;
    }

    public void setNumberOfDownloads(Integer numberOfDownloads) {
        this.numberOfDownloads = numberOfDownloads;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * toString method
     */
    @Override
    public String toString() {
        return "Filez{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", numberOfDownloads=" + numberOfDownloads +
                ", size=" + size +
                ", ownerId=" + ownerId +
                '}';
    }
}
