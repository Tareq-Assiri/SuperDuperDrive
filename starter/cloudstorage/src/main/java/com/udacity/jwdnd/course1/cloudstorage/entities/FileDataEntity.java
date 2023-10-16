package com.udacity.jwdnd.course1.cloudstorage.entities;

import java.sql.Blob;


public class FileDataEntity {
    private Blob fileData;

    
    public FileDataEntity(Blob fileData) {
        this.fileData = fileData;
    }

    
    public Blob getFileData() {
        return fileData;
    }

    public void setFileData(Blob fileData) {
        this.fileData = fileData;
    }
}
