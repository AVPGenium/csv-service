package ru.apolyakov.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.apolyakov.entity.UploadFile;

import java.util.List;

public interface UploadFileRepository extends CrudRepository<UploadFile, Long> {
    UploadFile getById(Long id);
    List<UploadFile> findAll();
}
