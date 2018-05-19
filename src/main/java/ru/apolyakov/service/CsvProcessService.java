package ru.apolyakov.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.apolyakov.entity.User;

import java.io.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class CsvProcessService {
    private static final Logger logger = LoggerFactory.getLogger(CsvProcessService.class);

    private final RestTemplate restTemplate;

    @Autowired
    DBFileUploadService dbFileUploadService;

    public CsvProcessService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Async
    public CompletableFuture<User> findUser(String user) throws InterruptedException {
        logger.info("Looking up " + user);
        String url = String.format("https://api.github.com/users/%s", user);
        User results = restTemplate.getForObject(url, User.class);
        // Artificial delay of 1s for demonstration purposes
        Thread.sleep(1000L);
        return CompletableFuture.completedFuture(results);
    }

    public void process(MultipartFile file, int maxResult, String sortColumnName){
        try //(ByteArrayOutputStream bos = new ByteArrayOutputStream())
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String result = CsvParser.process(reader, maxResult, sortColumnName);
            dbFileUploadService.store(result.getBytes(), file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] loadFromDb(byte[] obj)
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(obj);
        BufferedReader reader = new BufferedReader(new InputStreamReader(byteArrayInputStream));
        String csv = CsvParser.parse(reader);
        return csv.getBytes();
    }
}
