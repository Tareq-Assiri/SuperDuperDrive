package com.udacity.jwdnd.course1.cloudstorage.models;

import java.sql.Blob;


public class File {
    private Integer fileId;
    private String fileName;
    private String contentType;
    private long fileSize;
    private Integer userId;

    private Blob fileData;

    
    public File(Integer fileId, String fileName, String contentType, long fileSize, Integer userId, Blob fileData) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.userId = userId;
        this.fileData = fileData;
    }

    
    public Integer getFileId() {
        return fileId;
    }

    
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    
    public String getFileName() {
        return fileName;
    }

    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    
    public String getContentType() {
        return contentType;
    }

    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    
    public long getFileSize() {
        return fileSize;
    }

    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    
    public Integer getUserId() {
        return userId;
    }

    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    
    public Blob getFileData() {
        return fileData;
    }

    
    public void setFileData(Blob fileData) {
        this.fileData = fileData;
    }
}
