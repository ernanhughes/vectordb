package com.fodala.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public interface UploadService {
    void init();

    String store(MultipartFile file);

    Stream<String> loadAll();

    Resource loadAsResource(String filename);

    void deleteAll();

    void deleteFile(String filename);
}
