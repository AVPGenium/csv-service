package ru.apolyakov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.apolyakov.entity.UploadFile;
import ru.apolyakov.exceptions.StorageFileNotFoundException;
import ru.apolyakov.service.CsvProcessService;
import ru.apolyakov.service.DBFileUploadService;
import ru.apolyakov.service.StorageService;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final CsvProcessService csvProcessService;
    private final DBFileUploadService dbFileUploadService;

    @Autowired
    public FileUploadController(StorageService storageService, CsvProcessService csvProcessService, DBFileUploadService dbFileUploadService) {
        this.storageService = storageService;
        this.csvProcessService = csvProcessService;
        this.dbFileUploadService = dbFileUploadService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("files", dbFileUploadService.loadAll().stream().map(
                uploadFile -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", uploadFile.getFileName().toString(), uploadFile.getId()).build().toString())
                .collect(Collectors.toList()));
        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,
                                              @RequestParam("id") long id) {
        UploadFile file = dbFileUploadService.load(id);
        ByteArrayResource resource = new ByteArrayResource(csvProcessService.loadFromDb(file.getData()));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFileName() + "\"").body(resource);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("countN") int maxResult,
                                   @RequestParam("columnName") String sortColumnName,
                                   RedirectAttributes redirectAttributes) {

        csvProcessService.process(file, maxResult, sortColumnName);
        //storageService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
