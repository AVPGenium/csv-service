package ru.apolyakov.entity;

import javax.persistence.*;

@Entity
@Table
public class UploadFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String fileName;
    @Lob
    private byte[] data;

    public UploadFile() {
    }

    public UploadFile(String filename, byte[] data) {
        this.fileName = filename;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
