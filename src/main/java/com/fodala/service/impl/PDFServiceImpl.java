package com.fodala.service.impl;

import com.fodala.Application;
import com.fodala.service.PDFService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PDFServiceImpl implements PDFService {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Override
    public String toText(String filePath) {
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
            return e.getLocalizedMessage();
        }
    }
}
