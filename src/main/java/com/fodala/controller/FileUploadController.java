package com.fodala.controller;

import com.fodala.exception.StorageException;
import com.fodala.mapper.UploadedMapper;
import com.fodala.pojo.UploadedFile;
import com.fodala.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/todo")
public class FileUploadController {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final UploadService uploadService;
    private final UploadedMapper uploadedMapper;

    public FileUploadController(@Autowired UploadService uploadService, @Autowired UploadedMapper uploadedMapper) {
        this.uploadService = uploadService;
        this.uploadedMapper = uploadedMapper;
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) {
        model.addAttribute("files", uploadService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", new File(path).toPath().getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "upload";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = uploadService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        uploadService.store(file);
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/upload";
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageException exc) {
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/files")
    public String uploads(Model model) {
        List<UploadedFile> settings = uploadedMapper.all();
        logger.info("Files: {}", settings.toArray());
        model.addAttribute("files", settings);
        return "upload";
    }
}