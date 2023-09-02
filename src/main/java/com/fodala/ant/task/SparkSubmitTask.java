package com.fodala.ant.task;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SparkSubmitTask extends Task {
    private static final Logger logger = LoggerFactory.getLogger(SparkSubmitTask.class);
    private String filePath;
    private String opPath;

    public void setOpPath(String opPath) {
        this.opPath = opPath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void execute() throws BuildException {
        logger.info("PDFToTextTask executed with \nfilePath: {} \nopPath: {}", filePath, opPath);
        try (PDDocument document = PDDocument.load(new File(filePath))) {
            logger.info("Loaded file \nfilePath: {}", filePath);
            PDFTextStripper pdfStripper = new PDFTextStripper();
            Path path = Paths.get(opPath);
            String text = pdfStripper.getText(document);
            logger.info("Converted to text \n------------\ntext: {}\n------------", text);
            byte[] buf = text.getBytes();
            Files.write(path, buf);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }
}
