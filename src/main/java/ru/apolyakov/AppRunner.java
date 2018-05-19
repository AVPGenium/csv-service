package ru.apolyakov;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.apolyakov.entity.User;
import ru.apolyakov.service.CsvProcessService;

import java.util.concurrent.CompletableFuture;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    private final CsvProcessService csvProcessService;

    public AppRunner(CsvProcessService csvProcessService) {
        this.csvProcessService = csvProcessService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Start the clock
        long start = System.currentTimeMillis();

        // Kick of multiple, asynchronous lookups
        CompletableFuture<User> page1 = csvProcessService.findUser("PivotalSoftware");
        CompletableFuture<User> page2 = csvProcessService.findUser("CloudFoundry");
        CompletableFuture<User> page3 = csvProcessService.findUser("Spring-Projects");

        // Wait until they are all done
        CompletableFuture.allOf(page1,page2,page3).join();

        // Print results, including elapsed time
        logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("--> " + page1.get());
        logger.info("--> " + page2.get());
        logger.info("--> " + page3.get());

    }

}
