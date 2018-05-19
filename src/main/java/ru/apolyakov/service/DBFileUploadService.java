package ru.apolyakov.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.apolyakov.entity.UploadFile;
import ru.apolyakov.repository.UploadFileRepository;

import java.util.List;

@Service
@Transactional
public class DBFileUploadService{
    @Autowired
    UploadFileRepository fileUploadRepository;

    public void store(byte[] data, String filename)
    {
        UploadFile file = new UploadFile(filename, data);
        fileUploadRepository.save(file);
    }

    public UploadFile load(Long id)
    {
        return fileUploadRepository.getById(id);
    }

    public List<UploadFile> loadAll()
    {
        return fileUploadRepository.findAll();
    }

    public void deleteAll()
    {
        fileUploadRepository.deleteAll();
    }
}
